package currencyconverter.web.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class ExchangeRateModel {

    private final LocalDate date;
    private final String baseCurrency;
    private final Map<String, BigDecimal> exchangeRates;

    public ExchangeRateModel(Map<String, BigDecimal> exchangeRates, String baseCurrency, LocalDate date) {
        this.exchangeRates = exchangeRates;
        this.baseCurrency = baseCurrency;
        this.date = date;
    }

    public Map<String, BigDecimal> getExchangeRates() {
        return exchangeRates;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRateModel that = (ExchangeRateModel) o;
        return Objects.equals(exchangeRates, that.exchangeRates) &&
                Objects.equals(baseCurrency, that.baseCurrency) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchangeRates, baseCurrency, date);
    }

    @Override
    public String toString() {
        return "ExchangeRateModel{" +
                "exchangeRates=" + exchangeRates +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", date=" + date +
                '}';
    }

}
