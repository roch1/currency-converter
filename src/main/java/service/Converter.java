package service;

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

    public ConverterResponse convert(Rate eur2Source, Rate eur2Target, BigDecimal amount) {
        BigDecimal converted = convert(eur2Source.getFxRate(), eur2Target.getFxRate(), amount);

        String sourceCurrCode = eur2Source.getCurrency().getCurrencyCode();
        String targetCurrCode = eur2Target.getCurrency().getCurrencyCode();

        String source2TargetFxRate = "1 "
                + sourceCurrCode
                + " = "
                + converted.divide(amount, mc)
                + " "
                + targetCurrCode;

        return new ConverterResponse(sourceCurrCode, targetCurrCode, amount, source2TargetFxRate,
                converted, eur2Source.getCurrency().getDisplayName(), eur2Target.getCurrency().getDisplayName());
    }

    private BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, mc);
        BigDecimal sourceToEur = amount.multiply(sourceEurRate, mc);
        return sourceToEur.multiply(targetRate, mc);
    }

}
