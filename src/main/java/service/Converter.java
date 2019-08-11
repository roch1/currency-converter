package service;

import data.Rates;
import domain.CurrencyPair;
import domain.CurrencySingle;
import domain.Rate;
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
        LOGGER.debug("convert request: {} {} to {}", amount, sourceCurrCode, targetCurrCode);
        if (amount.compareTo(BigDecimal.ONE) < 0 ) {
            return zero(sourceCurrCode, targetCurrCode, amount);
        }

        CurrencySingle source = getCurrency(sourceCurrCode);
        CurrencySingle target = getCurrency(targetCurrCode);

        CurrencyPair currencyPair;
        if (source.getRate() == null || target.getRate() == null) {
            currencyPair = invalid(source, target, amount);
        } else {
            BigDecimal converted = convert(source.getRate(), target.getRate(), amount);
            currencyPair = valid(source, target, amount, converted);
        }

        return currencyPair;
    }

    private BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, MC);
        BigDecimal sourceToEur = amount.multiply(sourceEurRate, MC);
        return sourceToEur.multiply(targetRate, MC);
    }

    private CurrencyPair valid(CurrencySingle source, CurrencySingle target, BigDecimal amount, BigDecimal converted) {
        String srcCurrCode = source.getCurrency().getCurrencyCode();
        String trgtCurrCode = target.getCurrency().getCurrencyCode();
        String message = String.format("1 %s = %.6f %s", srcCurrCode, converted.divide(amount, MC), trgtCurrCode);
        return new CurrencyPair(srcCurrCode, trgtCurrCode, source.getCurrency().getDisplayName(), target.getCurrency().getDisplayName(),
                amount, converted, rates.getLastUpdated(), message);
    }

    private CurrencyPair invalid(CurrencySingle source, CurrencySingle target, BigDecimal amount) {
        String message = "One or more exchange rates are not available, cannot complete currency conversion request";
        return new CurrencyPair(source.getCurrency().getCurrencyCode(), target.getCurrency().getCurrencyCode(), amount, message);
    }

    private CurrencyPair zero(String sourceCurrCode, String targetCurrCode, BigDecimal amount) {
        String message = String.format("0 %s = 0 %s", sourceCurrCode, targetCurrCode);
        return new CurrencyPair(sourceCurrCode, targetCurrCode, getDisplayName(sourceCurrCode), getDisplayName(targetCurrCode),
                amount, BigDecimal.ZERO, rates.getLastUpdated(), message);
    }

    private CurrencySingle getCurrency(String currencyCode) {
        Rate currency = rates.getCurrency(currencyCode);
        Optional<BigDecimal> rate = rates.getFxRate(currency);
        return rate.map(r -> new CurrencySingle(currency, r))
                .orElseGet(() -> new CurrencySingle(currency, null));
    }

    private String getDisplayName(String currencyCode) {
        return rates.getCurrency(currencyCode).getDisplayName();
    }

}
