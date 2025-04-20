package com.rideease.patterns.factory;

/**
 * Factory Pattern: Product Types
 * Part of the Factory Pattern implementation
 */
public enum VehicleType {
    SEDAN("Sedan", 4),
    SUV("SUV", 6),
    HATCHBACK("Hatchback", 4),
    LUXURY("Luxury", 4),
    AUTO_RICKSHAW("Auto Rickshaw", 3);
    
    private final String displayName;
    private final int defaultCapacity;
    
    VehicleType(String displayName, int defaultCapacity) {
        this.displayName = displayName;
        this.defaultCapacity = defaultCapacity;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getDefaultCapacity() {
        return defaultCapacity;
    }
}
