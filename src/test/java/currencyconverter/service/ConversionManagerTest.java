package currencyconverter.service;

import currencyconverter.data.DataStore;
import currencyconverter.domain.ConverterResponse;
import currencyconverter.domain.Currency;
import currencyconverter.domain.CurrencyPair;
import currencyconverter.domain.ExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConversionManagerTest {

    private DataStore dataStore;
    private CurrencyConverter currencyConverter;
    private ConversionManager conversionManager;

    @BeforeEach
    void setUp() {
        dataStore = mock(DataStore.class);
        currencyConverter = mock(CurrencyConverter.class);
        conversionManager = new ConversionManager(dataStore, currencyConverter);
    }

    @Test
    @DisplayName("Convert £10 to US dollars")
    void givenAValidRequest_SourceAmountIsConvertedToTargetCurrency() {
        String sourceCurrencyCode = "GBP";
        String targetCurrencyCode = "USD";
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal convertedAmount = BigDecimal.valueOf(20L);
        LocalDateTime lastUpdated = LocalDateTime.of(2019, Month.AUGUST, 1, 0, 0);

        Currency gbp = new Currency(sourceCurrencyCode, "£", "British Pound");
        BigDecimal gbpRate = BigDecimal.ONE;
        Currency usd = new Currency(targetCurrencyCode, "$", "US Dollar");
        BigDecimal usdRate = BigDecimal.valueOf(2L);

        ExchangeRate source = new ExchangeRate(gbp, gbpRate);
        ExchangeRate target = new ExchangeRate(usd, usdRate);
        CurrencyPair pair = new CurrencyPair(source, target, usdRate);

        when(dataStore.getCurrency(sourceCurrencyCode)).thenReturn(gbp);
        when(dataStore.getCurrency(targetCurrencyCode)).thenReturn(usd);
        when(dataStore.getFxRate(gbp)).thenReturn(Optional.of(gbpRate));
        when(dataStore.getFxRate(usd)).thenReturn(Optional.of(usdRate));
        when(dataStore.getLastUpdated()).thenReturn(lastUpdated);
        when(currencyConverter.convert(gbpRate, usdRate, amount)).thenReturn(convertedAmount);
        when(currencyConverter.getQuotation(amount, convertedAmount)).thenReturn(target.getRate());

        ConverterResponse expected = new ConverterResponse(pair, amount, convertedAmount, lastUpdated.toLocalDate(), true);
        ConverterResponse actual = conversionManager.convertCurrency(sourceCurrencyCode, targetCurrencyCode, amount);

        assertThat(actual, is(expected));
    }

}