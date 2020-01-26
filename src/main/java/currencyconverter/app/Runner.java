package currencyconverter.app;

import currencyconverter.data.Rates;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.DataFeedManager;
import currencyconverter.data.feeds.EuropeanCentralBank;
import currencyconverter.service.Converter;
import currencyconverter.service.RateScheduler;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Runner {

    public static void main(String[] args) {
        Rates rates = new Rates();

        List<DataFeed> dataFeeds = Arrays.asList(new EuropeanCentralBank());
        DataFeedManager dataFeedManager = new DataFeedManager(dataFeeds);

        RateScheduler runner = new RateScheduler(rates, dataFeedManager);
        runner.startScheduling();

        Converter converter = new Converter(rates);

        // test cases
        System.out.println(converter.convert("hjk", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("USD", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("USd", "GbP", BigDecimal.TEN));
        System.out.println(converter.convert(null, null, null));
        System.out.println(converter.convert("USD", "JPY", BigDecimal.ZERO));

    }

}
