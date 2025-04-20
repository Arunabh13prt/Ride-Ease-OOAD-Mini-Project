package com.rideease.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Strategy for calculating ride prices
 * Implements Strategy Pattern without explicit interfaces
 */
@Component
public class PricingStrategy {
    
    // Base fare
    private static final double BASE_FARE = 50.0;
    
    // Per kilometer rate
    private static final double PER_KM_RATE = 10.0;
    
    // Surge pricing multipliers
    private static final double PEAK_HOUR_MULTIPLIER = 1.5;
    private static final double NIGHT_HOUR_MULTIPLIER = 1.2;
    
    /**
     * Calculate price based on distance and time
     */
    public double calculatePrice(double distanceInKm, LocalDateTime time) {
        // Calculate base price
        double basePrice = BASE_FARE + (distanceInKm * PER_KM_RATE);
        
        // Apply surge pricing if applicable
        double finalPrice = applySurgePricing(basePrice, time);
        
        // Round to 2 decimal places
        return Math.round(finalPrice * 100.0) / 100.0;
    }
    
    /**
     * Apply surge pricing based on time of day
     */
    private double applySurgePricing(double basePrice, LocalDateTime time) {
        LocalTime localTime = time.toLocalTime();
        
        // Peak hours: 8-10 AM and 5-8 PM
        boolean isPeakHour = (localTime.isAfter(LocalTime.of(8, 0)) && localTime.isBefore(LocalTime.of(10, 0))) ||
                             (localTime.isAfter(LocalTime.of(17, 0)) && localTime.isBefore(LocalTime.of(20, 0)));
        
        // Night hours: 11 PM - 5 AM
        boolean isNightHour = localTime.isAfter(LocalTime.of(23, 0)) || localTime.isBefore(LocalTime.of(5, 0));
        
        if (isPeakHour) {
            return basePrice * PEAK_HOUR_MULTIPLIER;
        } else if (isNightHour) {
            return basePrice * NIGHT_HOUR_MULTIPLIER;
        } else {
            return basePrice;
        }
    }
}
