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

class ConverterTest {

    private DataStore dataStore;
    private Converter converter;

    @BeforeEach
    void setUp() {
        dataStore = mock(DataStore.class);
        converter = new Converter(dataStore);
    }

    @Test
    @DisplayName("Convert £10 to US dollars")
    void givenAValidRequest_SourceAmountIsConvertedToTargetCurrency() {
        String sourceCurrencyCode = "GBP";
        String targetCurrencyCode = "USD";
        BigDecimal amount = BigDecimal.TEN;

        Currency gbp = new Currency(sourceCurrencyCode, "£", "British Pound");
        BigDecimal gbpRate = BigDecimal.ONE;
        Currency usd = new Currency(targetCurrencyCode, "$", "US Dollar");
        BigDecimal usdRate = BigDecimal.valueOf(2L);
        LocalDateTime lastUpdated = LocalDateTime.of(2019, Month.AUGUST, 1, 0, 0);

        when(dataStore.getCurrency(sourceCurrencyCode)).thenReturn(gbp);
        when(dataStore.getCurrency(targetCurrencyCode)).thenReturn(usd);
        when(dataStore.getFxRate(gbp)).thenReturn(Optional.of(gbpRate));
        when(dataStore.getFxRate(usd)).thenReturn(Optional.of(usdRate));
        when(dataStore.getLastUpdated()).thenReturn(lastUpdated);

        ExchangeRate source = new ExchangeRate(gbp, gbpRate);
        ExchangeRate target = new ExchangeRate(usd, usdRate);
        CurrencyPair pair = new CurrencyPair(source, target, usdRate);

        ConverterResponse expected = new ConverterResponse(pair, amount, BigDecimal.valueOf(20L), lastUpdated.toLocalDate(), true);
        ConverterResponse actual = converter.convert(sourceCurrencyCode, targetCurrencyCode, amount);

        assertThat(actual, is(expected));
    }

}