package com.rideease.repository;

import com.rideease.model.Vehicle;
import com.rideease.model.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    List<Vehicle> findByType(VehicleType type);
    boolean existsByRegistrationNumber(String registrationNumber);
}
