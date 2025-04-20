package com.rideease.controller;

import com.rideease.model.Rating;
import com.rideease.model.Ride;
import com.rideease.service.RatingService;
import com.rideease.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for Rating-related operations
 * Part of MVC architecture: Acts as Controller
 * Team Member: Member 4
 */
@Component
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final RideService rideService;

    public Rating addUserRating(Long rideId, int rating, String comment) {
        Ride ride = rideService.findRideById(rideId);
        return ratingService.addUserRating(ride, rating, comment);
    }
    
    public Rating addDriverRating(Long rideId, int rating, String comment) {
        Ride ride = rideService.findRideById(rideId);
        return ratingService.addDriverRating(ride, rating, comment);
    }
    
    public Rating getRatingById(Long id) {
        return ratingService.findRatingById(id);
    }
    
    public Rating getRatingByRideId(Long rideId) {
        Ride ride = rideService.findRideById(rideId);
        return ratingService.findRatingByRide(ride);
    }
    
    public List<Rating> getRatingsByUserId(Long userId) {
        return ratingService.findRatingsByUserId(userId);
    }
    
    public List<Rating> getRatingsByDriverId(Long driverId) {
        return ratingService.findRatingsByDriverId(driverId);
    }
    
    public List<Rating> getAllRatings() {
        return ratingService.findAllRatings();
    }
    
    public double getAverageDriverRating(Long driverId) {
        return ratingService.calculateAverageDriverRating(driverId);
    }
    
    public double getAverageUserRating(Long userId) {
        return ratingService.calculateAverageUserRating(userId);
    }
}
