package domain;

import java.util.Objects;

// value type
public final class Rate {

    private final String currencyCode;
    private final String symbol;
    private final String displayName;

    public Rate(String currencyCode, String symbol, String displayName) {
        this.currencyCode = currencyCode;
        this.symbol = symbol;
        this.displayName = displayName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "currencyCode='" + currencyCode + '\'' +
                ", symbol='" + symbol + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(currencyCode, rate.currencyCode) &&
                Objects.equals(symbol, rate.symbol) &&
                Objects.equals(displayName, rate.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, symbol, displayName);
    }

}
