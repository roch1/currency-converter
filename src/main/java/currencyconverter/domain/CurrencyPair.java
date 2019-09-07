package currencyconverter.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

// value type
public final class CurrencyPair {

    private final CurrencySingle source;
    private final CurrencySingle target;
    private final BigDecimal requestAmount;
    private final BigDecimal convertedAmount;
    private final LocalDate lastUpdated;
    private final String message;

    public CurrencyPair(CurrencySingle source, CurrencySingle target, BigDecimal requestAmount, BigDecimal convertedAmount, LocalDate lastUpdated, String message) {
        this.source = source;
        this.target = target;
        this.requestAmount = requestAmount;
        this.convertedAmount = convertedAmount;
        this.lastUpdated = lastUpdated;
        this.message = message;
    }

    public CurrencyPair(CurrencySingle source, CurrencySingle target, BigDecimal requestAmount, String message) {
        this(source, target, requestAmount, null, null, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(target, that.target) &&
                Objects.equals(requestAmount, that.requestAmount) &&
                Objects.equals(convertedAmount, that.convertedAmount) &&
                Objects.equals(lastUpdated, that.lastUpdated) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, requestAmount, convertedAmount, lastUpdated, message);
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
                "source=" + source +
                ", target=" + target +
                ", requestAmount=" + requestAmount +
                ", convertedAmount=" + convertedAmount +
                ", lastUpdated=" + lastUpdated +
                ", message='" + message + '\'' +
                '}';
    }

}
