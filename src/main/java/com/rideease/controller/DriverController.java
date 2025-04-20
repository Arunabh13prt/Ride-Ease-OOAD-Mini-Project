package com.rideease.controller;

import com.rideease.model.Driver;
import com.rideease.model.Vehicle;
import com.rideease.model.enums.VehicleType;
import com.rideease.patterns.factory.VehicleFactory;
import com.rideease.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for Driver-related operations
 * Part of MVC architecture: Acts as Controller
 * Team Member: Member 2
 */
@Component
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final VehicleFactory vehicleFactory;

    public Driver registerDriver(String name, String phoneNumber, String licenseNumber, 
                                 String vehicleModel, String registrationNumber,
                                 VehicleType vehicleType, String color, int capacity) {
        
        // Create vehicle using factory pattern
        Vehicle vehicle = vehicleFactory.createVehicle(vehicleType, vehicleModel, registrationNumber, color, capacity);
        
        // Create driver
        Driver driver = Driver.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .licenseNumber(licenseNumber)
                .available(true)
                .averageRating(0.0)
                .vehicle(vehicle)
                .build();
        
        return driverService.registerDriver(driver);
    }

    public Driver getDriverById(Long id) {
        return driverService.findDriverById(id);
    }

    public Driver getDriverByPhoneNumber(String phoneNumber) {
        return driverService.findDriverByPhoneNumber(phoneNumber);
    }

    public Driver getDriverByLicenseNumber(String licenseNumber) {
        return driverService.findDriverByLicenseNumber(licenseNumber);
    }

    public List<Driver> getAllDrivers() {
        return driverService.findAllDrivers();
    }

    public List<Driver> getAvailableDrivers() {
        return driverService.findAvailableDrivers();
    }

    public Driver updateDriverProfile(Long id, String name, String phoneNumber, String licenseNumber) {
        // Get existing driver
        Driver driver = driverService.findDriverById(id);
        
        // Update fields
        driver.setName(name);
        driver.setPhoneNumber(phoneNumber);
        driver.setLicenseNumber(licenseNumber);
        
        return driverService.updateDriver(driver);
    }

    public void updateDriverAvailability(Long id, boolean available) {
        driverService.updateDriverAvailability(id, available);
    }

    public void deleteDriver(Long id) {
        driverService.deleteDriver(id);
    }
}
