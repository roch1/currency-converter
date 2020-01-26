package currencyconverter.app;

import currencyconverter.data.DataStore;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.DataFeedManager;
import currencyconverter.data.feeds.EuropeanCentralBank;
import currencyconverter.service.Converter;
import currencyconverter.service.RateScheduler;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CurrencyConverter {

    public static void main(String[] args) {
        DataStore datastore = new DataStore();

        List<DataFeed> dataFeeds = Arrays.asList(new EuropeanCentralBank());
        DataFeedManager dataFeedManager = new DataFeedManager(dataFeeds);

        RateScheduler runner = new RateScheduler(datastore, dataFeedManager);
        runner.startScheduling();

        Converter converter = new Converter(datastore);

        // test cases
        System.out.println(converter.convert("hjk", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("USD", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("USd", "GbP", BigDecimal.TEN));
        System.out.println(converter.convert(null, null, null));
        System.out.println(converter.convert("USD", "JPY", BigDecimal.ZERO));

    }

}
