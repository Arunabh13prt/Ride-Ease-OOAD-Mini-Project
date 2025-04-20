package com.rideease.util;

import org.springframework.stereotype.Component;

/**
 * Utility class to calculate distance between two points using Haversine formula
 */
@Component
public class DistanceCalculator {
    
    private static final double EARTH_RADIUS_KM = 6371.0;
    
    /**
     * Calculate distance between two points in kilometers using Haversine formula
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);
        
        // Haversine formula
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;
        
        // Round to 2 decimal places
        return Math.round(distance * 100.0) / 100.0;
    }
}
