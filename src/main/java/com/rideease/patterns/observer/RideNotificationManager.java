package com.rideease.patterns.observer;

import com.rideease.model.Ride;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern: Concrete Subject
 * This class maintains a list of observers and notifies them when a ride status changes
 * Team Member: Member 1
 */
@Component
public class RideNotificationManager implements RideSubject {
    
    private final List<RideObserver> observers = new ArrayList<>();
    
    @Override
    public void attach(RideObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void detach(RideObserver observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(Ride ride) {
        for (RideObserver observer : observers) {
            observer.update(ride);
        }
    }
    
    public void notifyRideStatusChange(Ride ride) {
        notifyObservers(ride);
    }
}