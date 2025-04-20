package com.rideease.repository;

import com.rideease.model.Driver;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByUser(User user);
    List<Ride> findByUserOrderByCreatedAtDesc(User user);
    List<Ride> findByDriver(Driver driver);
    List<Ride> findByDriverOrderByCreatedAtDesc(Driver driver);
    List<Ride> findByStatus(RideStatus status);
    List<Ride> findByUserAndStatus(User user, RideStatus status);
    List<Ride> findByDriverAndStatus(Driver driver, RideStatus status);
}
