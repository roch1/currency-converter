package model;

import java.math.BigDecimal;
import java.util.Objects;

// value type
public final class Rate {

    private final String currencyCode;
    private final String symbol;
    private final String displayName;
    private final BigDecimal fxRate;

    public Rate(String currencyCode, String symbol, String displayName, BigDecimal fxRate) {
        this.currencyCode = currencyCode;
        this.symbol = symbol;
        this.displayName = displayName;
        this.fxRate = fxRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getFxRate() {
        return fxRate;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "currencyCode='" + currencyCode + '\'' +
                ", symbol='" + symbol + '\'' +
                ", displayName='" + displayName + '\'' +
                ", fxRate=" + fxRate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(currencyCode, rate.currencyCode) &&
                Objects.equals(symbol, rate.symbol) &&
                Objects.equals(displayName, rate.displayName) &&
                Objects.equals(fxRate, rate.fxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, symbol, displayName, fxRate);
    }

}
