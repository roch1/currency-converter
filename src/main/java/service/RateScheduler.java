package service;

import data.Rates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RateScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateScheduler.class);
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    private final Rates rates;
    private final RateGetter rateGetter;

    public RateScheduler(Rates rates, RateGetter rateGetter) {
        this.rates = rates;
        this.rateGetter = rateGetter;
    }

    public void startScheduling() {
        LOGGER.info("starting rate scheduler (should only happen once, on application start-up)");

        // rates should be populated as soon application starts up (only once), and then subsequently according to delay
        if (rates.empty()) {
            LOGGER.info("rates data source empty");
            rateGetter.getRates(rates);
            rates.putRate("EUR", "1"); // add base rate EUR to rates
        }

        scheduleTask(task());
    }

    private Runnable task() {
        return () -> {
            rateGetter.getRates(rates);
            scheduleTask(task()); // as part of the task, it should schedule itself to run again
        };
    }

    private void scheduleTask(Runnable task) {
        long interval = getInterval();
        EXECUTOR_SERVICE.schedule(task, interval, TimeUnit.SECONDS);
    }

    private long getInterval() {
        ZonedDateTime timeNow = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime targetTime = getUpdateTime();
        if (timeNow.isAfter(targetTime)) {
            targetTime.plusDays(1);
        }
        Duration duration = Duration.between(timeNow, targetTime);
        return duration.getSeconds();
    }

    private ZonedDateTime getUpdateTime() {
        LocalTime updateTime = LocalTime.of(16, 1);
        ZonedDateTime cet = ZonedDateTime.now(ZoneId.of("Europe/Paris")).with(updateTime);
        return cet.withZoneSameInstant(ZoneId.systemDefault()); // convert CET update time to local system time
    }

}
