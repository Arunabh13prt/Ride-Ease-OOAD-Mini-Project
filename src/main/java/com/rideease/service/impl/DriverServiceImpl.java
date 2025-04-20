package com.rideease.service.impl;

import com.rideease.exception.DriverNotFoundException;
import com.rideease.model.Driver;
import com.rideease.model.Vehicle;
import com.rideease.repository.DriverRepository;
import com.rideease.repository.RatingRepository;
import com.rideease.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of DriverService interface
 * Follows Dependency Inversion Principle (DIP) from SOLID
 */
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final RatingRepository ratingRepository;

    @Override
    public Driver registerDriver(Driver driver) {
        // Check if driver already exists
        if (driverRepository.existsByPhoneNumber(driver.getPhoneNumber())) {
            throw new IllegalArgumentException("Driver with this phone number already exists");
        }
        if (driverRepository.existsByLicenseNumber(driver.getLicenseNumber())) {
            throw new IllegalArgumentException("Driver with this license number already exists");
        }
        
        // Set default values
        driver.setAvailable(true);
        driver.setAverageRating(0.0);
        
        return driverRepository.save(driver);
    }

    @Override
    public Driver findDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + id));
    }

    @Override
    public Driver findDriverByPhoneNumber(String phoneNumber) {
        return driverRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with phone number: " + phoneNumber));
    }

    @Override
    public Driver findDriverByLicenseNumber(String licenseNumber) {
        return driverRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with license number: " + licenseNumber));
    }

    @Override
    public List<Driver> findAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public List<Driver> findAvailableDrivers() {
        return driverRepository.findByAvailableTrue();
    }

    @Override
    public Driver updateDriver(Driver driver) {
        // Check if driver exists
        Driver existingDriver = findDriverById(driver.getId());
        
        // Update driver details
        existingDriver.setName(driver.getName());
        existingDriver.setPhoneNumber(driver.getPhoneNumber());
        existingDriver.setLicenseNumber(driver.getLicenseNumber());
        existingDriver.setAvailable(driver.isAvailable());
        
        return driverRepository.save(existingDriver);
    }

    @Override
    public void deleteDriver(Long id) {
        // Check if driver exists
        Driver driver = findDriverById(id);
        driverRepository.delete(driver);
    }

    @Override
    public void assignVehicleToDriver(Driver driver, Vehicle vehicle) {
        driver.setVehicle(vehicle);
        driverRepository.save(driver);
    }

    @Override
    public void updateDriverAvailability(Long driverId, boolean available) {
        Driver driver = findDriverById(driverId);
        driver.setAvailable(available);
        driverRepository.save(driver);
    }

    @Override
    public void updateDriverRating(Long driverId) {
        Double averageRating = ratingRepository.getAverageDriverRating(driverId);
        if (averageRating != null) {
            Driver driver = findDriverById(driverId);
            driver.setAverageRating(averageRating);
            driverRepository.save(driver);
        }
    }
}
