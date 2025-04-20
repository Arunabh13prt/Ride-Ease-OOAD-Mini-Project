package com.rideease.patterns.command;

import com.rideease.controller.RideController;

/**
 * Command Pattern: Concrete Command for canceling a ride
 * Part of the Command Pattern implementation
 */
public class CancelRideCommand implements Command {
    
    private final RideController rideController;
    private final Long rideId;
    
    public CancelRideCommand(RideController rideController, Long rideId) {
        this.rideController = rideController;
        this.rideId = rideId;
    }
    
    @Override
    public Object execute() {
        return rideController.cancelRide(rideId);
    }
    
    @Override
    public String toString() {
        return "CancelRideCommand{" +
                "rideId=" + rideId +
                '}';
    }
}
