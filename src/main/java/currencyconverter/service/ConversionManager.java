package currencyconverter.service;

import currencyconverter.data.DataStore;
import currencyconverter.domain.ConverterResponse;
import currencyconverter.domain.Currency;
import currencyconverter.domain.CurrencyPair;
import currencyconverter.domain.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

public class ConversionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionManager.class);
    private final DataStore datastore;
    private final CurrencyConverter currencyConverter;

    public ConversionManager(DataStore dataStore, CurrencyConverter currencyConverter) {
        this.datastore = dataStore;
        this.currencyConverter = currencyConverter;
    }

    public ConverterResponse convertCurrency(String sourceCurrCode, String targetCurrCode, BigDecimal amount) {
        LOGGER.debug("request: convert {} {} to {}", amount, sourceCurrCode, targetCurrCode);
        ExchangeRate source = getCurrency(sourceCurrCode);
        ExchangeRate target = getCurrency(targetCurrCode);

        if (amount.compareTo(BigDecimal.ONE) < 0) {
            return valid(source, target, amount, BigDecimal.ZERO);
        } else if (source.getRate() == null || target.getRate() == null) {
            return invalid(source, target, amount);
        }

        BigDecimal converted = currencyConverter.convert(source.getRate(), target.getRate(), amount);
        return valid(source, target, amount, converted);
    }

    private ConverterResponse valid(ExchangeRate source, ExchangeRate target, BigDecimal requestAmount, BigDecimal convertedAmount) {
        BigDecimal quotation = currencyConverter.getQuotation(requestAmount, convertedAmount);
        CurrencyPair pair = new CurrencyPair(source, target, quotation);
        return new ConverterResponse(pair, requestAmount, convertedAmount, datastore.getLastUpdated().toLocalDate(), true);
    }

    private ConverterResponse invalid(ExchangeRate source, ExchangeRate target, BigDecimal requestAmount) {
        CurrencyPair pair = new CurrencyPair(source, target, null);
        return new ConverterResponse(pair, requestAmount, null, datastore.getLastUpdated().toLocalDate(), false);
    }

    private ExchangeRate getCurrency(String currencyCode) {
        // the same CurrencySingle object gets created multiple times a day
        Currency currency = datastore.getCurrency(currencyCode);
        Optional<BigDecimal> rate = datastore.getFxRate(currency);
        return rate.map(r -> new ExchangeRate(currency, r))
                .orElseGet(() -> new ExchangeRate(currency, null));
    }

}
