package currencyconverter.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class ExchangeRate {

    private final Currency currency;
    private final BigDecimal rate;

    public ExchangeRate(Currency currency, BigDecimal rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public Currency getCurrency() {
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
        ExchangeRate that = (ExchangeRate) o;
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
