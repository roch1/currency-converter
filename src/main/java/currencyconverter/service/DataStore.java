package currencyconverter.service;

import currencyconverter.domain.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// could be a singleton? - consider any impact to testing

public class DataStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataStore.class);
    private static final int INITIAL_CAPACITY = 32;
    private static final Map<String, Currency> INSTANCES = new ConcurrentHashMap<>(INITIAL_CAPACITY);
    private static final Map<Currency, BigDecimal> RATES = new HashMap<>(INITIAL_CAPACITY);
    private static LocalDateTime LAST_UPDATED = LocalDateTime.now().minusDays(4);

    public LocalDateTime getLastUpdated() {
        return LAST_UPDATED;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        LAST_UPDATED = lastUpdated;
    }

    public boolean empty() {
        return RATES.isEmpty();
    }

    public void putRate(String currencyCode, String fxRate) {
        RATES.put(getCurrency(currencyCode), new BigDecimal(fxRate));
    }

    public Optional<BigDecimal> getFxRate(Currency currency) {
        return Optional.ofNullable(RATES.get(currency));
    }

    public Set<Map.Entry<Currency, BigDecimal>> selectAll() {
        return new HashSet<>(RATES.entrySet()); // return new set so datastore cannot be modified outside of this class
    }

    public Currency baseCurrency() {
        return getCurrency("EUR");
    }

    public Currency getCurrency(String currencyCode) {
        Currency instance = INSTANCES.get(currencyCode);
        if (instance != null) {
            return instance;
        }

        return createCurrency(currencyCode);
    }

    private Currency createCurrency(String currencyCode) {
        LOGGER.debug("creating currency {}", currencyCode);

        Currency r;
        try {
            java.util.Currency currency = java.util.Currency.getInstance(currencyCode);
            Currency rate = new Currency(currencyCode, currency.getSymbol(), currency.getDisplayName());
            Currency instance = INSTANCES.putIfAbsent(currencyCode, rate);
            r = instance != null ? instance : rate;
        } catch (IllegalArgumentException e) {
            String invalidCurrCode = "invalid currency code [" + currencyCode + "]";
            LOGGER.error(invalidCurrCode, e);
            r = new Currency(invalidCurrCode, invalidCurrCode, invalidCurrCode);
        }

        return r;
    }

}
