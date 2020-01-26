package currencyconverter.web.app.configuration;

import currencyconverter.data.DataStore;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.EuropeanCentralBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import currencyconverter.service.Converter;
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
    public Converter converter() {
        LOGGER.info("creating Converter object - should only happen once");
        return new Converter(dataStore());
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

}
