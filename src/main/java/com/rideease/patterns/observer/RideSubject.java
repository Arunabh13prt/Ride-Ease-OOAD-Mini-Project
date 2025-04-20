package com.rideease.patterns.observer;

import com.rideease.model.Ride;

/**
 * Observer Pattern: Subject Interface
 * This interface defines methods for attaching, detaching, and notifying observers
 * Team Member: Member 1
 */
public interface RideSubject {
    void attach(RideObserver observer);
    void detach(RideObserver observer);
    void notifyObservers(Ride ride);
}