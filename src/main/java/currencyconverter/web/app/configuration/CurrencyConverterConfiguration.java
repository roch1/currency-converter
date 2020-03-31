package currencyconverter.web.app.configuration;

import currencyconverter.concurrent.CurrencyConverterThreadFactory;
import currencyconverter.http.CurrencyConverterHttpClient;
import currencyconverter.concurrent.CurrencyConverterThreadPool;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.EuropeanCentralBank;
import currencyconverter.service.ConversionManager;
import currencyconverter.service.Converter;
import currencyconverter.service.DataFeedManager;
import currencyconverter.service.DataStore;
import currencyconverter.service.RateScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Configuration
public class CurrencyConverterConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConverterConfiguration.class);

    @Bean
    public DataStore dataStore() {
        LOGGER.info("creating Data Store object - should only happen once");
        return new DataStore();
    }

    @Bean
    public ConversionManager conversionManager() {
        LOGGER.info("creating Conversion Manager object - should only happen once");
        return new ConversionManager(dataStore(), new Converter());
    }

    @Bean
    public void rateScheduler() {
        LOGGER.info("creating RateScheduler object - should only happen once");
        RateScheduler rateScheduler = new RateScheduler(dataStore(), dataFeedManager());
        rateScheduler.startScheduling();
    }

    private List<DataFeed> dataFeeds() {
        LOGGER.info("creating Data Feeds - should only happen once");
        CurrencyConverterThreadFactory threadFactory = new CurrencyConverterThreadFactory("EcbDfPool-");
        CurrencyConverterThreadPool threadPool = new CurrencyConverterThreadPool(2, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);
        CurrencyConverterHttpClient currencyConverterHttpClient = new CurrencyConverterHttpClient(threadPool);
        return Collections.singletonList(new EuropeanCentralBank(dataStore(), currencyConverterHttpClient));
    }

    private DataFeedManager dataFeedManager() {
        LOGGER.info("creating Data Feed Manager object - should only happen once");
        return new DataFeedManager(dataFeeds());
    }

}
