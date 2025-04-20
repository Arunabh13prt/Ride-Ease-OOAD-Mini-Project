package com.rideease.service;

import com.rideease.model.Driver;
import com.rideease.model.Location;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.RideStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface RideService {
    Ride bookRide(User user, Location pickupLocation, Location destinationLocation, LocalDateTime pickupTime);
    Ride assignDriverToRide(Ride ride, Driver driver);
    Ride startRide(Long rideId);
    Ride completeRide(Long rideId);
    Ride cancelRide(Long rideId);
    Ride findRideById(Long id);
    List<Ride> findRidesByUser(User user);
    List<Ride> findRidesByDriver(Driver driver);
    List<Ride> findRidesByStatus(RideStatus status);
    List<Ride> findRidesByDriverAndStatus(Driver driver, RideStatus status);
    List<Ride> findAllRides();
    double calculateFare(Ride ride);
    double calculateDistance(Location pickup, Location destination);
    Ride saveRide(Ride ride);
}
