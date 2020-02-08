package currencyconverter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CurrencyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConverter.class);
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_EVEN);

    public BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, MC);
        BigDecimal sourceEurAmount = amount.multiply(sourceEurRate, MC);
        return sourceEurAmount.multiply(targetRate, MC);
    }

    public BigDecimal getQuotation(BigDecimal amount, BigDecimal converted) {
        return converted.divide(amount, MC);
    }

}
