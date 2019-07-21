package service;

import data.Rates;
import domain.CurrencyPair;
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
        Rate eur2Source = rates.getCurrency(sourceCurrCode);
        Rate eur2Target = rates.getCurrency(targetCurrCode);

        LOGGER.debug("source: {}, target: {}", eur2Source, eur2Target);

        Optional<BigDecimal> sourceRate = rates.getRate(eur2Source);
        Optional<BigDecimal> targetRate = rates.getRate(eur2Target);

        LOGGER.debug("source fx rate {}:{}, target fx rate {}:{}", sourceCurrCode, sourceRate.orElse(null),
                targetCurrCode, targetRate.orElse(null));

        CurrencyPair currencyPair;
        if (sourceRate.isEmpty() || targetRate.isEmpty()) {
            currencyPair = new CurrencyPair(sourceCurrCode, targetCurrCode, eur2Source.getDisplayName(), eur2Target.getDisplayName(),
                    amount, null, rates.getLastUpdated(), null);
        } else {
            BigDecimal converted = convert(sourceRate.get(), targetRate.get(), amount);
            currencyPair = new CurrencyPair(sourceCurrCode, targetCurrCode, eur2Source.getDisplayName(), eur2Target.getDisplayName(),
                    amount, converted, rates.getLastUpdated(), converted.divide(amount, MC));
        }

        return currencyPair;
    }

    private BigDecimal convert(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amount) {
        BigDecimal sourceEurRate = BigDecimal.ONE.divide(sourceRate, MC);
        BigDecimal sourceToEur = amount.multiply(sourceEurRate, MC);
        return sourceToEur.multiply(targetRate, MC);
    }

}
