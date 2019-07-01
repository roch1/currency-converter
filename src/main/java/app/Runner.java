package app;

import service.Converter;
import service.RateGetter;
import data.Rates;

import java.math.BigDecimal;

public class Runner {

    public static void main(String[] args) {
        Rates rates = new Rates();
        Converter converter = new Converter(rates);
        new RateGetter().getRates(rates);

        // test cases
        System.out.println(converter.convert("GBP", "USD", BigDecimal.TEN));
        System.out.println(converter.convert("GBP", "GBP", BigDecimal.TEN));
        //System.out.println(converter.convert("EUR", "GBP", BigDecimal.TEN)); // fails with null pointer exception
    }

}
