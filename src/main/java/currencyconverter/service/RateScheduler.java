package currencyconverter.service;

import currencyconverter.data.Rates;
import currencyconverter.data.feeds.DataFeedManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
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
    private final DataFeedManager dataFeedManager;

    public RateScheduler(Rates rates, DataFeedManager dataFeedManager) {
        this.rates = rates;
        this.dataFeedManager = dataFeedManager;
    }

    public void startScheduling() {
        LOGGER.info("starting rate scheduler (should happen on application start-up only)");

        // rates should be populated as soon application starts up (only once), and then subsequently according to delay
        if (rates.empty()) {
            LOGGER.info("rates data source empty");
            dataFeedManager.getRates(rates);
            rates.putRate("EUR", "1"); // add base rate EUR to rates
        }

        scheduleTask(task());
    }

    private Runnable task() {
        return () -> {
            dataFeedManager.getRates(rates);
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
        LOGGER.debug("time now: {}, target time in local system time zone: {}", timeNow, targetTime);
        if (timeNow.isAfter(targetTime)) {
            targetTime = targetTime.plusDays(1); // ZonedDateTime is immutable, copy of object is returned, therefore needs to be assigned to original variable
        }
        LOGGER.debug("time now: {}, target time in local system time zone: {}", timeNow, targetTime);
        Duration duration = Duration.between(timeNow, targetTime);
        return duration.getSeconds();
    }

    private ZonedDateTime getUpdateTime() {
        LocalTime updateTime = LocalTime.of(16, 1);
        LOGGER.debug("update time {}", updateTime);
        ZonedDateTime centralEuropeanTimeZone = ZonedDateTime.of(LocalDate.now(), updateTime, ZoneId.of("Europe/Paris"));
        LOGGER.debug("update time in CET {}", centralEuropeanTimeZone);
        return centralEuropeanTimeZone.withZoneSameInstant(ZoneId.systemDefault()); // convert CET update time to local system time
    }

}
