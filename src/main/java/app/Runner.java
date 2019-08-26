package app;

import data.Rates;
import service.Converter;
import service.RateScheduler;
import service.RateGetter;

import java.math.BigDecimal;

public class Runner {

    public static void main(String[] args) {
        Rates rates = new Rates();
        RateGetter rateGetter = new RateGetter();

        RateScheduler runner = new RateScheduler(rates, rateGetter);
        runner.startScheduling();

        Converter converter = new Converter(rates);

        // test cases
        System.out.println(converter.convert("GBP", "USD", BigDecimal.TEN));
        System.out.println(converter.convert("GBP", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("EUR", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("hjk", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("USD", "GBP", BigDecimal.TEN));
        System.out.println(converter.convert("USd", "GbP", BigDecimal.TEN));
        System.out.println(converter.convert("USD", "JPY", BigDecimal.ZERO));
        System.out.println(converter.convert(null, null, null));

    }

}
