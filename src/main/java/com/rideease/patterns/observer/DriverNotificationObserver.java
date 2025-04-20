package com.rideease.patterns.observer;

import com.rideease.model.Ride;
import com.rideease.model.enums.RideStatus;
import org.springframework.stereotype.Component;

/**
 * Observer Pattern: Concrete Observer
 * This class represents an observer that sends notifications to drivers when ride status changes
 * Team Member: Member 1
 */
@Component
public class DriverNotificationObserver implements RideObserver {
    
    @Override
    public void update(Ride ride) {
        if (ride.getDriver() != null) {
            String message = createDriverNotificationMessage(ride);
            // In a real application, this would send an SMS, email, or push notification to the driver
            System.out.println("Notification to driver " + ride.getDriver().getName() + ": " + message);
        }
    }
    
    private String createDriverNotificationMessage(Ride ride) {
        RideStatus status = ride.getStatus();
        switch (status) {
            case DRIVER_ASSIGNED:
                return "You have been assigned a new ride. User: " + ride.getUser().getName() + 
                       ", Pickup: " + ride.getPickupAddress();
            case IN_PROGRESS:
                return "Your ride with " + ride.getUser().getName() + " is now in progress.";
            case COMPLETED:
                return "Ride completed successfully. Earned fare: $" + ride.getFare();
            case CANCELLED:
                return "Ride has been cancelled.";
            default:
                return "Ride status has been updated to " + status;
        }
    }
}