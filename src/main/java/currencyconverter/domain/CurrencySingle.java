package currencyconverter.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class CurrencySingle {

    private final Rate currency;
    private final BigDecimal rate;

    public CurrencySingle(Rate currency, BigDecimal rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public Rate getCurrency() {
        return currency;
    }

    public BigDecimal getRate() {
        if (rate == null) {
            return null;
        }
        return new BigDecimal(rate.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencySingle that = (CurrencySingle) o;
        return Objects.equals(currency, that.currency) &&
                Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, rate);
    }

    @Override
    public String toString() {
        return "CurrencySingle{" +
                "currency=" + currency +
                ", rate=" + rate +
                '}';
    }

}
