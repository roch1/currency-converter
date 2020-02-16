package currencyconverter.web.app.configuration;

import currencyconverter.service.DataStore;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.EuropeanCentralBank;
import currencyconverter.service.ConversionManager;
import currencyconverter.service.CurrencyConverter;
import currencyconverter.web.app.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import currencyconverter.data.feeds.DataFeedManager;
import currencyconverter.service.RateScheduler;

import java.util.Arrays;
import java.util.List;

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
        return new ConversionManager(dataStore(), new CurrencyConverter());
    }

    @Bean
    public DataFeedManager dataFeedManager() {
        LOGGER.info("creating Data Feed Manager object - should only happen once");
        return new DataFeedManager(dataFeeds());
    }

    @Bean
    public void rateScheduler() {
        LOGGER.info("creating RateScheduler object - should only happen once");
        RateScheduler rateScheduler = new RateScheduler(dataStore(), dataFeedManager());
        rateScheduler.startScheduling();
    }

    @Bean
    public List<DataFeed> dataFeeds() {
        return Arrays.asList(new EuropeanCentralBank());
    }

    @Bean
    public ExchangeRateService exchangeRateService() {
        LOGGER.info("creating Exchange Rate Service object - should only happen once");
        return new ExchangeRateService(dataStore(), conversionManager());
    }

}
