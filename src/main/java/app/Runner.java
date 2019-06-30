package app;

import service.Converter;
import service.RateGetter;
import data.Rates;

import java.math.BigDecimal;

public class Runner {

    public static void main(String[] args) {
        Rates rates = new Rates();
        Converter conversion = new Converter(rates);
        new RateGetter().getRates(rates);

        System.out.println(conversion.convert("GBP", "USD", BigDecimal.TEN));
    }

}
