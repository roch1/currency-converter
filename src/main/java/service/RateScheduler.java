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
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final Rates rates;
    private final RateGetter rateGetter;

    public RateScheduler(Rates rates, RateGetter rateGetter) {
        this.rates = rates;
        this.rateGetter = rateGetter;
    }

    public void startScheduling() {
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
        executorService.schedule(task, interval, TimeUnit.SECONDS);
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
