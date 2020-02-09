package currencyconverter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyConverterTest {

    private CurrencyConverter currencyConverter;

    @BeforeEach
    void setUp() {
        currencyConverter = new CurrencyConverter();
    }

    @ParameterizedTest
    @CsvSource({"1, 2, 10, 20", "0.84703, 1.1246, 10, 13.27697957"})
    void convertShouldReturnConvertedAmount(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amountToConvert, BigDecimal expected) {
        BigDecimal actual = currencyConverter.convert(sourceRate, targetRate, amountToConvert);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"10, 20, 2", "10, 13.27697957, 1.327697957"})
    void quotationShouldReturnQuotation(BigDecimal amount, BigDecimal converted, BigDecimal expected) {
        BigDecimal actual = currencyConverter.getQuotation(amount, converted);
        assertEquals(expected, actual);
    }

}