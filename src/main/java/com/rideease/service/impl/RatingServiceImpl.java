package com.rideease.service.impl;

import com.rideease.model.Driver;
import com.rideease.model.Rating;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.RideStatus;
import com.rideease.repository.RatingRepository;
import com.rideease.service.DriverService;
import com.rideease.service.RatingService;
import com.rideease.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of RatingService interface
 * Follows Dependency Inversion Principle (DIP) from SOLID
 */
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverService driverService;
    private final UserService userService;

    @Override
    public Rating addUserRating(Ride ride, int ratingValue, String comment) {
        // Check if ride is completed
        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot rate an incomplete ride. Current status: " + ride.getStatus());
        }
        
        // Validate rating value
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        // Get or create rating
        Rating rating = ratingRepository.findByRide(ride).orElse(new Rating());
        
        // Set user rating
        rating.setRide(ride);
        rating.setUser(ride.getUser());
        rating.setDriver(ride.getDriver());
        rating.setUserRating(ratingValue);
        rating.setUserComment(comment);
        
        Rating savedRating = ratingRepository.save(rating);
        
        // Update driver's average rating
        driverService.updateDriverRating(ride.getDriver().getId());
        
        return savedRating;
    }

    @Override
    public Rating addDriverRating(Ride ride, int ratingValue, String comment) {
        // Check if ride is completed
        if (ride.getStatus() != RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot rate an incomplete ride. Current status: " + ride.getStatus());
        }
        
        // Validate rating value
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        // Get or create rating
        Rating rating = ratingRepository.findByRide(ride).orElse(new Rating());
        
        // Set driver rating
        rating.setRide(ride);
        rating.setUser(ride.getUser());
        rating.setDriver(ride.getDriver());
        rating.setDriverRating(ratingValue);
        rating.setDriverComment(comment);
        
        Rating savedRating = ratingRepository.save(rating);
        
        // Update user's average rating
        userService.updateUserRating(ride.getUser().getId());
        
        return savedRating;
    }

    @Override
    public Rating findRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with id: " + id));
    }

    @Override
    public Rating findRatingByRide(Ride ride) {
        return ratingRepository.findByRide(ride)
                .orElse(null); // Return null instead of throwing exception if no rating exists yet
    }

    @Override
    public List<Rating> findRatingsByUserId(Long userId) {
        return ratingRepository.findByUser_Id(userId);
    }

    @Override
    public List<Rating> findRatingsByDriverId(Long driverId) {
        return ratingRepository.findByDriver_Id(driverId);
    }

    @Override
    public List<Rating> findAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public double calculateAverageDriverRating(Long driverId) {
        Double averageRating = ratingRepository.getAverageDriverRating(driverId);
        return averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0; // Round to 1 decimal place
    }

    @Override
    public double calculateAverageUserRating(Long userId) {
        Double averageRating = ratingRepository.getAverageUserRating(userId);
        return averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0; // Round to 1 decimal place
    }
}
