package com.rideease.repository;

import com.rideease.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByPhoneNumber(String phoneNumber);
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    List<Driver> findByAvailableTrue();
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByLicenseNumber(String licenseNumber);
}
