package com.rideease.patterns.builder;

import com.rideease.model.Driver;
import com.rideease.model.Location;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.RideStatus;

import java.time.LocalDateTime;

/**
 * Builder Pattern Implementation
 * Provides a way to construct a complex Ride object step by step
 * Team Member: Member 3
 */
public class RideBuilder {
    
    private User user;
    private Driver driver;
    private Location pickupLocation;
    private Location destinationLocation;
    private LocalDateTime pickupTime;
    private LocalDateTime dropOffTime;
    private double distance;
    private double fare;
    private RideStatus status;
    
    public RideBuilder() {
        // Default values
        this.status = RideStatus.REQUESTED;
    }
    
    // Builder methods
    public RideBuilder withUser(User user) {
        this.user = user;
        return this;
    }
    
    public RideBuilder withDriver(Driver driver) {
        this.driver = driver;
        return this;
    }
    
    public RideBuilder withPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
        return this;
    }
    
    public RideBuilder withDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
        return this;
    }
    
    public RideBuilder withPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
        return this;
    }
    
    public RideBuilder withDropOffTime(LocalDateTime dropOffTime) {
        this.dropOffTime = dropOffTime;
        return this;
    }
    
    public RideBuilder withDistance(double distance) {
        this.distance = distance;
        return this;
    }
    
    public RideBuilder withFare(double fare) {
        this.fare = fare;
        return this;
    }
    
    public RideBuilder withStatus(RideStatus status) {
        this.status = status;
        return this;
    }
    
    // Build method
    public Ride build() {
        // Validate required fields
        if (user == null) {
            throw new IllegalStateException("User cannot be null");
        }
        if (pickupLocation == null) {
            throw new IllegalStateException("Pickup location cannot be null");
        }
        if (destinationLocation == null) {
            throw new IllegalStateException("Destination location cannot be null");
        }
        
        // Create ride
        return Ride.builder()
                .user(user)
                .driver(driver)
                .pickupLocation(pickupLocation)
                .destinationLocation(destinationLocation)
                .pickupTime(pickupTime)
                .dropOffTime(dropOffTime)
                .distance(distance)
                .fare(fare)
                .status(status)
                .build();
    }
}
