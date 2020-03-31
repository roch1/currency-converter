package currencyconverter.app;

import currencyconverter.http.CurrencyConverterHttpClient;
import currencyconverter.concurrent.CurrencyConverterThreadFactory;
import currencyconverter.concurrent.CurrencyConverterThreadPool;
import currencyconverter.data.feeds.DataFeed;
import currencyconverter.data.feeds.EuropeanCentralBank;
import currencyconverter.service.ConversionManager;
import currencyconverter.service.Converter;
import currencyconverter.service.DataFeedManager;
import currencyconverter.service.DataStore;
import currencyconverter.service.RateScheduler;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CurrencyConverter {

    public void run() {
        DataStore dataStore = new DataStore();

        CurrencyConverterThreadFactory threadFactory = new CurrencyConverterThreadFactory("EcbDfPool-"); // thread name can only be 15 characters in total length
        CurrencyConverterThreadPool threadPool = new CurrencyConverterThreadPool(2, 4,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);

        EuropeanCentralBank ecbDataFeed = new EuropeanCentralBank(dataStore, new CurrencyConverterHttpClient(threadPool));
        List<DataFeed> dataFeeds = Collections.singletonList(ecbDataFeed);
        DataFeedManager dataFeedManager = new DataFeedManager(dataFeeds);

        RateScheduler runner = new RateScheduler(dataStore, dataFeedManager);
        runner.startScheduling();

        ConversionManager conversionManager = new ConversionManager(dataStore, new Converter());

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
