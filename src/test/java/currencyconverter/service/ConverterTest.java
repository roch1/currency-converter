package currencyconverter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterTest {

    private Converter converter;

    @BeforeEach
    void setUp() {
        converter = new Converter();
    }

    @ParameterizedTest
    @CsvSource({"1, 2, 10, 20", "0.84703, 1.1246, 10, 13.27697957"})
    void convertShouldReturnConvertedAmount(BigDecimal sourceRate, BigDecimal targetRate, BigDecimal amountToConvert, BigDecimal expected) {
        BigDecimal actual = converter.convert(sourceRate, targetRate, amountToConvert);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"10, 20, 2", "10, 13.27697957, 1.327697957"})
    void quotationShouldReturnQuotation(BigDecimal amount, BigDecimal converted, BigDecimal expected) {
        BigDecimal actual = converter.getQuotation(amount, converted);
        assertEquals(expected, actual);
    }

}