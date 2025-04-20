package com.rideease.patterns.observer;

import com.rideease.model.Ride;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Observer Pattern: Concrete Observer
 * This class represents an observer that logs all ride status changes for administrative purposes
 * Team Member: Member 1
 */
@Component
public class AdminLogObserver implements RideObserver {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void update(Ride ride) {
        String timestamp = LocalDateTime.now().format(formatter);
        String log = String.format("[%s] RIDE #%d - Status: %s, User: %s, Driver: %s, From: %s, To: %s, Fare: $%.2f",
                timestamp,
                ride.getId(),
                ride.getStatus(),
                ride.getUser() != null ? ride.getUser().getName() : "N/A",
                ride.getDriver() != null ? ride.getDriver().getName() : "N/A",
                ride.getPickupAddress(),
                ride.getDestinationAddress(),
                ride.getFare());
        
        // In a real application, this would be logged to a database or log file
        System.out.println("ADMIN LOG: " + log);
    }
}