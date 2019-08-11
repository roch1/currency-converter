package data;

import domain.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Rates {

    private static final Logger LOGGER = LoggerFactory.getLogger(Rates.class);
    private static final int INITIAL_CAPACITY = 32;
    private static final Map<String, Rate> INSTANCES = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    private final Map<Rate, BigDecimal> rates = new HashMap<>(INITIAL_CAPACITY);
    private LocalDate lastUpdated = LocalDate.now().minusDays(4);

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean empty() {
        return rates.isEmpty();
    }

    public void putRate(String currencyCode, String fxRate) {
        rates.put(getCurrency(currencyCode), new BigDecimal(fxRate));
    }

    public Optional<BigDecimal> getFxRate(Rate currency) {
        return Optional.ofNullable(rates.get(currency));
    }

    public Rate getCurrency(String currencyCode) {
        Rate instance = INSTANCES.get(currencyCode);
        if (instance != null) {
            return instance;
        }

        return createRate(currencyCode);
    }

    private Rate createRate(String currencyCode) {
        LOGGER.debug("creating rate {}", currencyCode);

        Rate r;
        try {
            Currency currency = Currency.getInstance(currencyCode);
            Rate rate = new Rate(currencyCode, currency.getSymbol(), currency.getDisplayName());
            Rate instance = INSTANCES.putIfAbsent(currencyCode, rate);
            r = instance != null ? instance : rate;
        } catch (IllegalArgumentException e) {
            String invalidCurrCode = "invalid currency code: " + currencyCode;
            LOGGER.error(invalidCurrCode, e);
            r = new Rate(invalidCurrCode, invalidCurrCode, invalidCurrCode);
        }

        return r;
    }

}
