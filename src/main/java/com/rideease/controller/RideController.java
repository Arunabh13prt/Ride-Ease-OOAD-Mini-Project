package com.rideease.controller;

import com.rideease.model.Driver;
import com.rideease.model.Location;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.RideStatus;
import com.rideease.patterns.builder.RideBuilder;
import com.rideease.service.DriverService;
import com.rideease.service.RideService;
import com.rideease.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for Ride-related operations
 * Part of MVC architecture: Acts as Controller
 * Team Member: Member 3
 */
@Component
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final UserService userService;
    private final DriverService driverService;

    public Ride bookRide(Long userId, Location pickup, Location destination, LocalDateTime pickupTime) {
        // Get user
        User user = userService.findUserById(userId);
        
        // Book ride
        return rideService.bookRide(user, pickup, destination, pickupTime);
    }
    
    public Ride bookRideUsingBuilder(Long userId, Location pickup, Location destination, LocalDateTime pickupTime) {
        // Get user
        User user = userService.findUserById(userId);
        
        // Use builder pattern to create a ride
        Ride ride = new RideBuilder()
                .withUser(user)
                .withPickupLocation(pickup)
                .withDestinationLocation(destination)
                .withPickupTime(pickupTime)
                .withStatus(RideStatus.REQUESTED)
                .withDistance(rideService.calculateDistance(pickup, destination))
                .build();
        
        // Calculate fare
        double fare = rideService.calculateFare(ride);
        ride.setFare(fare);
        
        // Save the ride
        return rideService.saveRide(ride);
    }

    public Ride assignDriverToRide(Long rideId, Long driverId) {
        // Get ride and driver
        Ride ride = rideService.findRideById(rideId);
        Driver driver = driverService.findDriverById(driverId);
        
        // Assign driver to ride
        return rideService.assignDriverToRide(ride, driver);
    }

    public Ride startRide(Long rideId) {
        return rideService.startRide(rideId);
    }

    public Ride completeRide(Long rideId) {
        return rideService.completeRide(rideId);
    }

    public Ride cancelRide(Long rideId) {
        return rideService.cancelRide(rideId);
    }

    public Ride getRideById(Long id) {
        return rideService.findRideById(id);
    }

    public List<Ride> getRidesByUser(Long userId) {
        User user = userService.findUserById(userId);
        return rideService.findRidesByUser(user);
    }

    public List<Ride> getRidesByDriver(Long driverId) {
        Driver driver = driverService.findDriverById(driverId);
        return rideService.findRidesByDriver(driver);
    }

    public List<Ride> getRidesByStatus(RideStatus status) {
        return rideService.findRidesByStatus(status);
    }
    
    /**
     * Get rides by driver ID and status
     * @param driverId the driver ID
     * @param status the ride status
     * @return list of rides matching the driver and status
     */
    public List<Ride> getRidesByDriverIdAndStatus(Long driverId, RideStatus status) {
        Driver driver = driverService.findDriverById(driverId);
        return rideService.findRidesByDriverAndStatus(driver, status);
    }

    public List<Ride> getAllRides() {
        return rideService.findAllRides();
    }

    public double calculateFare(Location pickup, Location destination, LocalDateTime pickupTime) {
        double distance = rideService.calculateDistance(pickup, destination);
        
        // Create a temporary ride object to calculate fare
        Ride tempRide = Ride.builder()
                .distance(distance)
                .pickupTime(pickupTime)
                .build();
        
        return rideService.calculateFare(tempRide);
    }
}
