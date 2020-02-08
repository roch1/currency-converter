package currencyconverter.app;

import currencyconverter.data.DataStore;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.DataFeedManager;
import currencyconverter.data.feeds.EuropeanCentralBank;
import currencyconverter.service.RateScheduler;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CurrencyConverter {

    public static void main(String[] args) {
        List<DataFeed> dataFeeds = Arrays.asList(new EuropeanCentralBank());
        DataFeedManager dataFeedManager = new DataFeedManager(dataFeeds);

        DataStore datastore = new DataStore();
        RateScheduler runner = new RateScheduler(datastore, dataFeedManager);
        runner.startScheduling();

        currencyconverter.service.CurrencyConverter currencyConverter = new currencyconverter.service.CurrencyConverter(datastore);

        // test cases
        System.out.println(currencyConverter.convert("hjk", "GBP", BigDecimal.TEN));
        System.out.println(currencyConverter.convert("USD", "GBP", BigDecimal.TEN));
        System.out.println(currencyConverter.convert("USd", "GbP", BigDecimal.TEN));
        System.out.println(currencyConverter.convert(null, null, null));
        System.out.println(currencyConverter.convert("USD", "JPY", BigDecimal.ZERO));

    }

}
