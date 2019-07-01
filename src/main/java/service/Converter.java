package service;

import data.Rates;
import model.ConverterResponse;
import model.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);
    private static final MathContext mc = new MathContext(10, RoundingMode.HALF_EVEN);
    private final Rates rates;

    public Converter(Rates rates) {
        this.rates = rates;
    }

    public ConverterResponse convert(String sourceCurrCode, String targetCurrCode, BigDecimal amount) {
        Rate eur2Source = rates.getRate(sourceCurrCode);
        Rate eur2Target = rates.getRate(targetCurrCode);

        BigDecimal sourceEurRate = BigDecimal.ONE.divide(eur2Source.getFxRate(), mc);

        BigDecimal sourceToEur = amount.multiply(sourceEurRate, mc);
        BigDecimal eurToTarget = sourceToEur.multiply(eur2Target.getFxRate(), mc);

        String source2TargetFxRate = "1 " + sourceCurrCode + " = " + eurToTarget.divide(amount, mc) + " " + targetCurrCode;

        ConverterResponse response = new ConverterResponse(sourceCurrCode, targetCurrCode, amount, source2TargetFxRate,
                eurToTarget, eur2Source.getCurrency().getDisplayName(), eur2Target.getCurrency().getDisplayName());

        return response;
    }

}
