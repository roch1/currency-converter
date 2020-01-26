package currencyconverter.domain;

import java.math.BigDecimal;
import java.util.Objects;

// value type
public final class CurrencyPair {

    private final ExchangeRate base;
    private final ExchangeRate quote;
    private final BigDecimal quotation;

    public CurrencyPair(ExchangeRate base, ExchangeRate quote, BigDecimal quotation) {
        this.base = base;
        this.quote = quote;
        this.quotation = quotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(base, that.base) &&
                Objects.equals(quote, that.quote) &&
                Objects.equals(quotation, that.quotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, quote, quotation);
    }

    @Override
    public String toString() {
        return base.getCurrency().getCurrencyCode() +
                "/" +
                quote.getCurrency().getCurrencyCode() +
                " " +
                quotation;
    }

}
