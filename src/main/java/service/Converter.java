package service;

import data.Rates;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Converter {

    private static final MathContext mc = new MathContext(6, RoundingMode.HALF_EVEN);
    private final Rates rates;

    public Converter(Rates rates) {
        this.rates = rates;
    }

    public BigDecimal convert(String from, String to, BigDecimal amount) {
        BigDecimal eurSourceRate = rates.getRate(from);
        BigDecimal eurTargetRate = rates.getRate(to);

        BigDecimal sourceEurRate = BigDecimal.ONE.divide(eurSourceRate, mc);

        BigDecimal sourceToEur = amount.multiply(sourceEurRate, mc);
        return sourceToEur.multiply(eurTargetRate, mc);
    }

}
