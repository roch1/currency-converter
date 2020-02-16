package currencyconverter.web.app.service;

import currencyconverter.domain.ConverterResponse;
import currencyconverter.domain.Currency;
import currencyconverter.service.ConversionManager;
import currencyconverter.service.DataStore;
import currencyconverter.web.app.model.ExchangeRateModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateService {

    private final DataStore datastore;
    private final ConversionManager conversionManager;

    public ExchangeRateService(DataStore datastore, ConversionManager conversionManager) {
        this.datastore = datastore;
        this.conversionManager = conversionManager;
    }

    public Currency getCurrency(String currencyCode) {
        return datastore.getCurrency(currencyCode);
    }

    public ConverterResponse convertCurrency(String baseCurrCode, String quoteCurrCode, BigDecimal amount) {
        return conversionManager.convertCurrency(baseCurrCode, quoteCurrCode, amount);
    }

    public ExchangeRateModel getLatestRates() {
        Map<String, BigDecimal> exchangeRates = new HashMap<>();
        String baseCurrencyCode = datastore.baseCurrency().getCurrencyCode();

        for (Map.Entry<Currency, BigDecimal> entry : datastore.selectAll()) {
            String currencyCode = entry.getKey().getCurrencyCode();
            if (!currencyCode.equals(baseCurrencyCode)) {
                exchangeRates.put(currencyCode, entry.getValue());
            }
        }

        return new ExchangeRateModel(exchangeRates, baseCurrencyCode, datastore.getLastUpdated().toLocalDate());
    }

}
