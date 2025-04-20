package com.rideease.service.impl;

import com.rideease.model.Driver;
import com.rideease.model.Rating;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.repository.RatingRepository;
import com.rideease.service.DriverService;
import com.rideease.service.RatingService;
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

    @Override
    public Rating addUserRating(Ride ride, int ratingValue, String comment) {
        // Check if ride is completed
        if (ride.getStatus() != com.rideease.model.enums.RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot rate an incomplete ride");
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
        if (ride.getStatus() != com.rideease.model.enums.RideStatus.COMPLETED) {
            throw new IllegalStateException("Cannot rate an incomplete ride");
        }
        
        // Get or create rating
        Rating rating = ratingRepository.findByRide(ride).orElse(new Rating());
        
        // Set driver rating
        rating.setRide(ride);
        rating.setUser(ride.getUser());
        rating.setDriver(ride.getDriver());
        rating.setDriverRating(ratingValue);
        rating.setDriverComment(comment);
        
        return ratingRepository.save(rating);
    }

    @Override
    public Rating findRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with id: " + id));
    }

    @Override
    public Rating findRatingByRide(Ride ride) {
        return ratingRepository.findByRide(ride)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found for ride: " + ride.getId()));
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
        return averageRating != null ? averageRating : 0.0;
    }

    @Override
    public double calculateAverageUserRating(Long userId) {
        Double averageRating = ratingRepository.getAverageUserRating(userId);
        return averageRating != null ? averageRating : 0.0;
    }
}
