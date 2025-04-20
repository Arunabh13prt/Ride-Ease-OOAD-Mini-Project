package com.rideease.service.impl;

import com.rideease.exception.DriverNotFoundException;
import com.rideease.exception.RideNotFoundException;
import com.rideease.model.Driver;
import com.rideease.model.Location;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.RideStatus;
import com.rideease.repository.DriverRepository;
import com.rideease.repository.RideRepository;
import com.rideease.service.RideService;
import com.rideease.util.DistanceCalculator;
import com.rideease.util.PricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of RideService interface
 * Follows Open/Closed Principle (OCP) from SOLID
 */
@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final DistanceCalculator distanceCalculator;
    private final PricingStrategy pricingStrategy;

    @Override
    public Ride bookRide(User user, Location pickupLocation, Location destinationLocation, LocalDateTime pickupTime) {
        // Create new ride
        Ride ride = Ride.builder()
                .user(user)
                .pickupLocation(pickupLocation)
                .destinationLocation(destinationLocation)
                .pickupTime(pickupTime)
                .status(RideStatus.REQUESTED)
                .distance(calculateDistance(pickupLocation, destinationLocation))
                .build();
        
        // Calculate and set fare
        double fare = calculateFare(ride);
        ride.setFare(fare);
        
        return rideRepository.save(ride);
    }

    @Override
    public Ride assignDriverToRide(Ride ride, Driver driver) {
        // Check if driver is available
        if (!driver.isAvailable()) {
            throw new IllegalStateException("Selected driver is not available");
        }
        
        // Assign driver to ride
        ride.setDriver(driver);
        ride.setStatus(RideStatus.DRIVER_ASSIGNED);
        
        // Update driver availability
        driver.setAvailable(false);
        driverRepository.save(driver);
        
        return rideRepository.save(ride);
    }

    @Override
    public Ride startRide(Long rideId) {
        Ride ride = findRideById(rideId);
        
        // Check if ride can be started
        if (ride.getStatus() != RideStatus.DRIVER_ASSIGNED) {
            throw new IllegalStateException("Ride cannot be started. Current status: " + ride.getStatus());
        }
        
        // Update ride status
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setPickupTime(LocalDateTime.now());
        
        return rideRepository.save(ride);
    }

    @Override
    public Ride completeRide(Long rideId) {
        Ride ride = findRideById(rideId);
        
        // Check if ride can be completed
        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ride cannot be completed. Current status: " + ride.getStatus());
        }
        
        // Update ride status
        ride.setStatus(RideStatus.COMPLETED);
        ride.setDropOffTime(LocalDateTime.now());
        
        // Make driver available again
        Driver driver = ride.getDriver();
        driver.setAvailable(true);
        driverRepository.save(driver);
        
        return rideRepository.save(ride);
    }

    @Override
    public Ride cancelRide(Long rideId) {
        Ride ride = findRideById(rideId);
        
        // Check if ride can be cancelled
        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new IllegalStateException("Ride cannot be cancelled. Current status: " + ride.getStatus());
        }
        
        // Update ride status
        ride.setStatus(RideStatus.CANCELLED);
        
        // Make driver available again if assigned
        if (ride.getDriver() != null) {
            Driver driver = ride.getDriver();
            driver.setAvailable(true);
            driverRepository.save(driver);
        }
        
        return rideRepository.save(ride);
    }

    @Override
    public Ride findRideById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + id));
    }

    @Override
    public List<Ride> findRidesByUser(User user) {
        return rideRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public List<Ride> findRidesByDriver(Driver driver) {
        return rideRepository.findByDriverOrderByCreatedAtDesc(driver);
    }

    @Override
    public List<Ride> findRidesByStatus(RideStatus status) {
        return rideRepository.findByStatus(status);
    }

    @Override
    public List<Ride> findAllRides() {
        return rideRepository.findAll();
    }

    @Override
    public double calculateFare(Ride ride) {
        return pricingStrategy.calculatePrice(ride.getDistance(), ride.getPickupTime());
    }

    @Override
    public double calculateDistance(Location pickup, Location destination) {
        return distanceCalculator.calculateDistance(
                pickup.getLatitude(), pickup.getLongitude(),
                destination.getLatitude(), destination.getLongitude()
        );
    }
    
    @Override
    public Ride saveRide(Ride ride) {
        return rideRepository.save(ride);
    }
}
