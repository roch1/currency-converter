package currencyconverter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_EVEN);

    public BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        LOGGER.debug("converting amount {} using source rate {} and target rate {}", amount, sourceRate, targetRate);
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, MC);
        BigDecimal sourceEurAmount = amount.multiply(sourceEurRate, MC);
        return sourceEurAmount.multiply(targetRate, MC);
    }

    public BigDecimal getQuotation(BigDecimal amount, BigDecimal converted) {
        // this could be saved back into the data store if it doesn't exist
        return converted.divide(amount, MC);
    }

}
