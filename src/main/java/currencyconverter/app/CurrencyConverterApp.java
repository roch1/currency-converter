package currencyconverter.app;

import currencyconverter.service.DataStore;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.DataFeedManager;
import currencyconverter.data.feeds.EuropeanCentralBank;
import currencyconverter.service.ConversionManager;
import currencyconverter.service.CurrencyConverter;
import currencyconverter.service.RateScheduler;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CurrencyConverterApp {

    public static void main(String[] args) {
        List<DataFeed> dataFeeds = Arrays.asList(new EuropeanCentralBank());
        DataFeedManager dataFeedManager = new DataFeedManager(dataFeeds);

        DataStore datastore = new DataStore();
        RateScheduler runner = new RateScheduler(datastore, dataFeedManager);
        runner.startScheduling();

        ConversionManager conversionManager = new ConversionManager(datastore, new CurrencyConverter());

        // test cases
        System.out.println(conversionManager.convertCurrency("hjk", "GBP", BigDecimal.TEN));
        System.out.println(conversionManager.convertCurrency("USD", "GBP", BigDecimal.TEN));
        System.out.println(conversionManager.convertCurrency("USd", "GbP", BigDecimal.TEN));
        System.out.println(conversionManager.convertCurrency("GBP", "USD", BigDecimal.TEN));
        System.out.println(conversionManager.convertCurrency("GBP", "EUR", BigDecimal.TEN));
        System.out.println(conversionManager.convertCurrency("EUR", "GBP", BigDecimal.TEN));
        System.out.println(conversionManager.convertCurrency(null, null, null));
        System.out.println(conversionManager.convertCurrency("USD", "JPY", BigDecimal.ZERO));

    }

}
