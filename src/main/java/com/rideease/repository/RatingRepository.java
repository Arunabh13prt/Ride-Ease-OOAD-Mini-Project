package com.rideease.repository;

import com.rideease.model.Driver;
import com.rideease.model.Rating;
import com.rideease.model.Ride;
import com.rideease.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUser(User user);
    List<Rating> findByDriver(Driver driver);
    Optional<Rating> findByRide(Ride ride);
    List<Rating> findByUser_Id(Long userId);
    List<Rating> findByDriver_Id(Long driverId);
    
    @Query("SELECT AVG(r.userRating) FROM Rating r WHERE r.driver.id = :driverId")
    Double getAverageDriverRating(Long driverId);
    
    @Query("SELECT AVG(r.driverRating) FROM Rating r WHERE r.user.id = :userId")
    Double getAverageUserRating(Long userId);
}
