package domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

// value type
public final class CurrencyPair {

    private final String sourceCurrCode;
    private final String targetCurrCode;
    private final String sourceDisplayName;
    private final String targetDisplayName;
    private final BigDecimal sourceAmount;
    private final BigDecimal convertedAmount;
    private final LocalDate lastUpdated;
    private final BigDecimal oneSourceToTargetRate;
    private final String message;

    public CurrencyPair(String sourceCurrCode, String targetCurrCode, String sourceDisplayName, String targetDisplayName,
                        BigDecimal sourceAmount, BigDecimal convertedAmount, LocalDate lastUpdated, BigDecimal oneSourceToTargetRate) {
        this.sourceCurrCode = sourceCurrCode;
        this.targetCurrCode = targetCurrCode;
        this.sourceDisplayName = sourceDisplayName;
        this.targetDisplayName = targetDisplayName;
        this.sourceAmount = sourceAmount;
        this.convertedAmount = convertedAmount;
        this.lastUpdated = lastUpdated;
        this.oneSourceToTargetRate = oneSourceToTargetRate;
        this.message = currencyPairMessage();
    }

    private String currencyPairMessage() {
        if (oneSourceToTargetRate == null) {
            return "One or more exchange rates are not available, cannot complete currency conversion request";
        }
        return String.format("1 %s = %.6f %s", sourceCurrCode, oneSourceToTargetRate, targetCurrCode);
    }

    public String getSourceCurrCode() {
        return sourceCurrCode;
    }

    public String getTargetCurrCode() {
        return targetCurrCode;
    }

    public String getSourceDisplayName() {
        return sourceDisplayName;
    }

    public String getTargetDisplayName() {
        return targetDisplayName;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
                "sourceCurrCode='" + sourceCurrCode + '\'' +
                ", targetCurrCode='" + targetCurrCode + '\'' +
                ", sourceDisplayName='" + sourceDisplayName + '\'' +
                ", targetDisplayName='" + targetDisplayName + '\'' +
                ", sourceAmount=" + sourceAmount +
                ", convertedAmount=" + convertedAmount +
                ", lastUpdated=" + lastUpdated +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(sourceCurrCode, that.sourceCurrCode) &&
                Objects.equals(targetCurrCode, that.targetCurrCode) &&
                Objects.equals(sourceDisplayName, that.sourceDisplayName) &&
                Objects.equals(targetDisplayName, that.targetDisplayName) &&
                Objects.equals(sourceAmount, that.sourceAmount) &&
                Objects.equals(convertedAmount, that.convertedAmount) &&
                Objects.equals(lastUpdated, that.lastUpdated) &&
                Objects.equals(oneSourceToTargetRate, that.oneSourceToTargetRate) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceCurrCode, targetCurrCode, sourceDisplayName, targetDisplayName, sourceAmount, convertedAmount,
                lastUpdated, oneSourceToTargetRate, message);
    }

}
