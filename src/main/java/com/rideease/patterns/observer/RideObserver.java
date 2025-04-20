package com.rideease.patterns.observer;

import com.rideease.model.Ride;

/**
 * Observer Pattern: Observer Interface
 * This interface defines the update method that is called when a ride status changes
 * Team Member: Member 1
 */
public interface RideObserver {
    void update(Ride ride);
}