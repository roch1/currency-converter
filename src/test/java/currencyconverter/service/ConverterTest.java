package currencyconverter.service;

import currencyconverter.data.Rates;
import currencyconverter.domain.ConverterResponse;
import currencyconverter.domain.CurrencyPair;
import currencyconverter.domain.CurrencySingle;
import currencyconverter.domain.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConverterTest {

    private Rates rates;
    private Converter converter;

    @BeforeEach
    void setUp() {
        rates = mock(Rates.class);
        converter = new Converter(rates);
    }

    @Test
    @DisplayName("Convert £10 to US dollars")
    void givenAValidRequest_SourceAmountIsConvertedToTargetCurrency() {
        String sourceCurrencyCode = "GBP";
        String targetCurrencyCode = "USD";
        BigDecimal amount = BigDecimal.TEN;

        Rate gbp = new Rate(sourceCurrencyCode, "£", "British Pound");
        BigDecimal gbpRate = BigDecimal.ONE;
        Rate usd = new Rate(targetCurrencyCode, "$", "US Dollar");
        BigDecimal usdRate = BigDecimal.valueOf(2L);
        LocalDate lastUpdated = LocalDate.of(2019, Month.AUGUST, 1);

        when(rates.getCurrency(sourceCurrencyCode)).thenReturn(gbp);
        when(rates.getCurrency(targetCurrencyCode)).thenReturn(usd);
        when(rates.getFxRate(gbp)).thenReturn(Optional.of(gbpRate));
        when(rates.getFxRate(usd)).thenReturn(Optional.of(usdRate));
        when(rates.getLastUpdated()).thenReturn(lastUpdated);

        CurrencySingle source = new CurrencySingle(gbp, gbpRate);
        CurrencySingle target = new CurrencySingle(usd, usdRate);
        CurrencyPair pair = new CurrencyPair(source, target, usdRate);

        ConverterResponse expected = new ConverterResponse(pair, amount, BigDecimal.valueOf(20L), lastUpdated, true);
        ConverterResponse actual = converter.convert(sourceCurrencyCode, targetCurrencyCode, amount);

        assertThat(actual, is(expected));
    }

}