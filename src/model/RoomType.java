package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum RoomType {
    SINGLE("Single", new BigDecimal("2000.00"), new BigDecimal("1.0")),
    DOUBLE("Double", new BigDecimal("2000.00"), new BigDecimal("1.2")),
    DELUXE("Deluxe", new BigDecimal("2000.00"), new BigDecimal("1.4")),
    SUITE("Suite",  new BigDecimal("2000.00"), new BigDecimal("1.6"));

    private final String label;
    private final BigDecimal baseRate;
    private final BigDecimal multiplier;

    RoomType(String label, BigDecimal baseRate, BigDecimal multiplier) {
        this.label = label;
        this.baseRate = baseRate;
        this.multiplier = multiplier;
    }

    public BigDecimal getNightlyRate() {
        return baseRate.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}

