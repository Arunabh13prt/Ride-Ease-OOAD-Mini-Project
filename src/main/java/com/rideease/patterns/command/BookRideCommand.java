package com.rideease.patterns.command;

import com.rideease.controller.RideController;
import com.rideease.model.Location;
import com.rideease.model.Ride;

import java.time.LocalDateTime;

/**
 * Command Pattern: Concrete Command for booking a ride
 * Part of the Command Pattern implementation
 */
public class BookRideCommand implements Command {
    
    private final RideController rideController;
    private final Long userId;
    private final Location pickup;
    private final Location destination;
    private final LocalDateTime pickupTime;
    
    public BookRideCommand(RideController rideController, Long userId, Location pickup, 
                           Location destination, LocalDateTime pickupTime) {
        this.rideController = rideController;
        this.userId = userId;
        this.pickup = pickup;
        this.destination = destination;
        this.pickupTime = pickupTime;
    }
    
    @Override
    public Object execute() {
        return rideController.bookRide(userId, pickup, destination, pickupTime);
    }
    
    @Override
    public String toString() {
        return "BookRideCommand{" +
                "userId=" + userId +
                ", pickup=" + pickup +
                ", destination=" + destination +
                ", pickupTime=" + pickupTime +
                '}';
    }
}
