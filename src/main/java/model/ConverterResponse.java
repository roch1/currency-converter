package model;

import java.math.BigDecimal;
import java.util.Objects;

// value type
public final class ConverterResponse {

    private final String sourceCurrCode;
    private final String targetCurrCode;
    private final BigDecimal sourceAmount;
    private final String sourceToTargetFxRate;
    private final BigDecimal convertedAmount;
    private final String sourceDisplayName;
    private final String targetDisplayName;

    public ConverterResponse(String sourceCurrCode, String targetCurrCode, BigDecimal sourceAmount, String sourceToTargetFxRate,
                             BigDecimal convertedAmount, String sourceDisplayName, String targetDisplayName) {
        this.sourceCurrCode = sourceCurrCode;
        this.targetCurrCode = targetCurrCode;
        this.sourceAmount = sourceAmount;
        this.sourceToTargetFxRate = sourceToTargetFxRate;
        this.convertedAmount = convertedAmount;
        this.sourceDisplayName = sourceDisplayName;
        this.targetDisplayName = targetDisplayName;
    }

    @Override
    public String toString() {
        return "ConverterResponse{" +
                "sourceCurrCode='" + sourceCurrCode + '\'' +
                ", targetCurrCode='" + targetCurrCode + '\'' +
                ", sourceAmount=" + sourceAmount +
                ", sourceToTargetFxRate='" + sourceToTargetFxRate + '\'' +
                ", convertedAmount=" + convertedAmount +
                ", sourceDisplayName='" + sourceDisplayName + '\'' +
                ", targetDisplayName='" + targetDisplayName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConverterResponse that = (ConverterResponse) o;
        return sourceCurrCode.equals(that.sourceCurrCode) &&
                targetCurrCode.equals(that.targetCurrCode) &&
                sourceAmount.equals(that.sourceAmount) &&
                sourceToTargetFxRate.equals(that.sourceToTargetFxRate) &&
                convertedAmount.equals(that.convertedAmount) &&
                sourceDisplayName.equals(that.sourceDisplayName) &&
                targetDisplayName.equals(that.targetDisplayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceCurrCode, targetCurrCode, sourceAmount, sourceToTargetFxRate, convertedAmount,
                sourceDisplayName, targetDisplayName);
    }

}
