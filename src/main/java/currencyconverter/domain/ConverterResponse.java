package currencyconverter.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ConverterResponse {

    private final CurrencyPair pair;
    private final BigDecimal requestAmount;
    private final BigDecimal convertedAmount;
    private final LocalDate lastUpdated;
    private final boolean valid;

    public ConverterResponse(CurrencyPair pair, BigDecimal requestAmount, BigDecimal convertedAmount, LocalDate lastUpdated, boolean valid) {
        this.pair = pair;
        this.requestAmount = requestAmount;
        this.convertedAmount = convertedAmount;
        this.lastUpdated = lastUpdated;
        this.valid = valid;
    }

    public CurrencyPair getPair() {
        return pair;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConverterResponse that = (ConverterResponse) o;
        return valid == that.valid &&
                Objects.equals(pair, that.pair) &&
                Objects.equals(requestAmount, that.requestAmount) &&
                Objects.equals(convertedAmount, that.convertedAmount) &&
                Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, requestAmount, convertedAmount, lastUpdated, valid);
    }

    @Override
    public String toString() {
        return "ConverterResponse{" +
                "pair=" + pair +
                ", requestAmount=" + requestAmount +
                ", convertedAmount=" + convertedAmount +
                ", lastUpdated=" + lastUpdated +
                ", valid=" + valid +
                '}';
    }

}
