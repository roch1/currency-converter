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

        Converter converter = new Converter();

        // test cases
        System.out.println(converter.convert(rates.getRate("GBP"), rates.getRate("USD"), BigDecimal.TEN));
        System.out.println(converter.convert(rates.getRate("GBP"), rates.getRate("GBP"), BigDecimal.TEN));
        System.out.println(converter.convert(rates.getRate("EUR"), rates.getRate("GBP"), BigDecimal.TEN));

    }

}
