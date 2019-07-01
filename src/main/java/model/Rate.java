package model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class Rate {

    private final Currency currency;
    private final BigDecimal fxRate;

    public Rate(Currency currency, BigDecimal fxRate) {
        this.currency = currency;
        this.fxRate = fxRate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getFxRate() {
        return fxRate;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "currency=" + currency +
                ", fxRate=" + fxRate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return currency.equals(rate.currency) &&
                fxRate.equals(rate.fxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, fxRate);
    }

}
