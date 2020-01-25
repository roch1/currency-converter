package currencyconverter.service;

import currencyconverter.data.Rates;
import currencyconverter.domain.ConverterResponse;
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

    public ConverterResponse convert(String sourceCurrCode, String targetCurrCode, BigDecimal amount) {
        LOGGER.debug("request: convert {} {} to {}", amount, sourceCurrCode, targetCurrCode);
        CurrencySingle source = getCurrency(sourceCurrCode);
        CurrencySingle target = getCurrency(targetCurrCode);

        if (amount.compareTo(BigDecimal.ONE) < 0) {
            return valid(source, target, amount, BigDecimal.ZERO);
        } else if (source.getRate() == null || target.getRate() == null) {
            return invalid(source, target, amount);
        }

        BigDecimal converted = convert(source.getRate(), target.getRate(), amount);
        return valid(source, target, amount, converted);
    }

    private BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, MC);
        BigDecimal sourceEurAmount = amount.multiply(sourceEurRate, MC);
        return sourceEurAmount.multiply(targetRate, MC);
    }

    private ConverterResponse valid(CurrencySingle source, CurrencySingle target, BigDecimal requestAmount, BigDecimal convertedAmount) {
        CurrencyPair pair = new CurrencyPair(source, target, getQuotation(requestAmount, convertedAmount));
        return new ConverterResponse(pair, requestAmount, convertedAmount, rates.getLastUpdated(), true);
    }

    private ConverterResponse invalid(CurrencySingle source, CurrencySingle target, BigDecimal requestAmount) {
        CurrencyPair pair = new CurrencyPair(source, target, null);
        return new ConverterResponse(pair, requestAmount, null, rates.getLastUpdated(), false);
    }

    private BigDecimal getQuotation(BigDecimal amount, BigDecimal converted) {
        return converted.divide(amount, MC);
    }

    private CurrencySingle getCurrency(String currencyCode) {
        // the same CurrencySingle object gets created multiple times a day
        Rate currency = rates.getCurrency(currencyCode);
        Optional<BigDecimal> rate = rates.getFxRate(currency);
        return rate.map(r -> new CurrencySingle(currency, r))
                .orElseGet(() -> new CurrencySingle(currency, null));
    }

}
