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
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_EVEN);

    public ConverterResponse convert(Rate eur2Source, Rate eur2Target, BigDecimal amount) {
        BigDecimal converted = convert(eur2Source.getFxRate(), eur2Target.getFxRate(), amount);

        String sourceCurrCode = eur2Source.getCurrencyCode();
        String targetCurrCode = eur2Target.getCurrencyCode();

        String source2TargetFxRate = String.format("1 %s = %.6f %s",
                sourceCurrCode, converted.divide(amount, MC), targetCurrCode);

        return new ConverterResponse(sourceCurrCode, targetCurrCode, amount, converted, source2TargetFxRate,
                eur2Source.getDisplayName(), eur2Target.getDisplayName());
    }

    private BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, MC);
        BigDecimal sourceToEur = amount.multiply(sourceEurRate, MC);
        return sourceToEur.multiply(targetRate, MC);
    }

}
