package currencyconverter.service;

import currencyconverter.data.Rates;
import currencyconverter.domain.CurrencyPair;
import currencyconverter.domain.CurrencySingle;
import currencyconverter.domain.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

public class Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_EVEN);
    private final Rates rates;

    public Converter(Rates rates) {
        this.rates = rates;
    }

    public CurrencyPair convert(String sourceCurrCode, String targetCurrCode, BigDecimal amount) {
        LOGGER.debug("request: convert {} {} to {}", amount, sourceCurrCode, targetCurrCode);
        CurrencySingle source = getCurrency(sourceCurrCode);
        CurrencySingle target = getCurrency(targetCurrCode);

        if (amount.compareTo(BigDecimal.ONE) < 0) {
            return valid(source, target, amount, BigDecimal.ZERO, ConverterMessage.ZERO, BigDecimal.ZERO);
        } else if (source.getRate() == null || target.getRate() == null) {
            return invalid(source, target, amount);
        }

        BigDecimal converted = convert(source.getRate(), target.getRate(), amount);
        return valid(source, target, amount, converted, ConverterMessage.VALID, converted.divide(amount, MC));
    }

    private BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, MC);
        BigDecimal sourceEurAmount = amount.multiply(sourceEurRate, MC);
        return sourceEurAmount.multiply(targetRate, MC);
    }

    private CurrencyPair valid(CurrencySingle source, CurrencySingle target, BigDecimal requestAmount,
                                     BigDecimal convertedAmount, ConverterMessage message, BigDecimal quotation) {
        String srcCurrCode = source.getCurrency().getCurrencyCode();
        String targetCurrCode = target.getCurrency().getCurrencyCode();
        String m = message.formatMessage(srcCurrCode, quotation, targetCurrCode);
        return new CurrencyPair(source, target, requestAmount, convertedAmount, rates.getLastUpdated(), m);
    }

    private CurrencyPair invalid(CurrencySingle source, CurrencySingle target, BigDecimal requestAmount) {
        return new CurrencyPair(source, target, requestAmount, ConverterMessage.INVALID.getMessage());
    }

    private CurrencySingle getCurrency(String currencyCode) {
        // the same CurrencySingle object gets created multiple times a day
        Rate currency = rates.getCurrency(currencyCode);
        Optional<BigDecimal> rate = rates.getFxRate(currency);
        return rate.map(r -> new CurrencySingle(currency, r))
                .orElseGet(() -> new CurrencySingle(currency, null));
    }

    private enum ConverterMessage {
        VALID("1 %s = %.6f %s"),
        INVALID("One or more exchange rates are not available, cannot complete currency conversion request"),
        ZERO("0 %s = %.6f %s")
        ;

        private String message;

        ConverterMessage(String message) {
            this.message = message;
        }

        String getMessage() {
            return message;
        }

        String formatMessage(String sourceCurrCode, BigDecimal convertedAmount, String targetCurrCode) {
            return String.format(message, sourceCurrCode, convertedAmount, targetCurrCode);
        }
    }

}
