package com.rideease.repository;

import com.rideease.model.Payment;
import com.rideease.model.Ride;
import com.rideease.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);
    List<Payment> findByRide_User(User user);
    List<Payment> findByRide_User_Id(Long userId);
    List<Payment> findByRide_Driver_Id(Long driverId);
    List<Payment> findByCompletedTrue();
    List<Payment> findByCompletedFalse();
}
