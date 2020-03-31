package currencyconverter.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CurrencyConverterThreadFactory implements ThreadFactory {

    private final AtomicInteger threadCount = new AtomicInteger(0);
    private final String threadNamePrefix;

    public CurrencyConverterThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(new ThreadGroup("CurrConvThreadFactoryThreadGroup"), r, getNextThreadName());
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setDaemon(false);
        return thread;
    }

    private String getDefaultThreadNamePrefix() {
        return getClass().getName() + "-";
    }

    private String getNextThreadName() {
        return threadNamePrefix + threadCount.incrementAndGet();
    }

}
