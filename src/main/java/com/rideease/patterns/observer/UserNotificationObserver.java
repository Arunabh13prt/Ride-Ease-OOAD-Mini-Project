package com.rideease.patterns.observer;

import com.rideease.model.Ride;
import com.rideease.model.enums.RideStatus;
import org.springframework.stereotype.Component;

/**
 * Observer Pattern: Concrete Observer
 * This class represents an observer that sends notifications to users when ride status changes
 * Team Member: Member 1
 */
@Component
public class UserNotificationObserver implements RideObserver {
    
    @Override
    public void update(Ride ride) {
        if (ride.getUser() != null) {
            String message = createUserNotificationMessage(ride);
            // In a real application, this would send an SMS, email, or push notification to the user
            System.out.println("Notification to user " + ride.getUser().getName() + ": " + message);
        }
    }
    
    private String createUserNotificationMessage(Ride ride) {
        RideStatus status = ride.getStatus();
        switch (status) {
            case REQUESTED:
                return "Your ride request has been received. We're finding a driver for you.";
            case DRIVER_ASSIGNED:
                return "A driver has been assigned to your ride. Driver: " + ride.getDriver().getName();
            case IN_PROGRESS:
                return "Your ride is now in progress. Enjoy your trip!";
            case COMPLETED:
                return "Your ride has been completed. Total fare: $" + ride.getFare() + ". Please rate your experience.";
            case CANCELLED:
                return "Your ride has been cancelled.";
            default:
                return "Your ride status has been updated to " + status;
        }
    }
}