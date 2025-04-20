package com.rideease.service;

import com.rideease.model.Rating;
import com.rideease.model.Ride;

import java.util.List;

public interface RatingService {
    Rating addUserRating(Ride ride, int rating, String comment);
    Rating addDriverRating(Ride ride, int rating, String comment);
    Rating findRatingById(Long id);
    Rating findRatingByRide(Ride ride);
    List<Rating> findRatingsByUserId(Long userId);
    List<Rating> findRatingsByDriverId(Long driverId);
    List<Rating> findAllRatings();
    double calculateAverageDriverRating(Long driverId);
    double calculateAverageUserRating(Long userId);
}
