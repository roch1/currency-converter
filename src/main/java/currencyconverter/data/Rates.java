package currencyconverter.data;

import currencyconverter.domain.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Rates could be a singleton? - consider any impact to testing

public class Rates {

    private static final Logger LOGGER = LoggerFactory.getLogger(Rates.class);
    private static final int INITIAL_CAPACITY = 32;
    private static final Map<String, Rate> INSTANCES = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    private static final Map<Rate, BigDecimal> RATES = new HashMap<>(INITIAL_CAPACITY);
    private static LocalDate LAST_UPDATED = LocalDate.now().minusDays(4);

    public LocalDate getLastUpdated() {
        return LAST_UPDATED;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        LAST_UPDATED = lastUpdated;
    }

    public boolean empty() {
        return RATES.isEmpty();
    }

    public void putRate(String currencyCode, String fxRate) {
        RATES.put(getCurrency(currencyCode), new BigDecimal(fxRate));
    }

    public Optional<BigDecimal> getFxRate(Rate currency) {
        return Optional.ofNullable(RATES.get(currency));
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
