package com.rideease.service;

import com.rideease.model.Driver;
import com.rideease.model.Vehicle;

import java.util.List;

public interface DriverService {
    Driver registerDriver(Driver driver);
    Driver findDriverById(Long id);
    Driver findDriverByPhoneNumber(String phoneNumber);
    Driver findDriverByLicenseNumber(String licenseNumber);
    List<Driver> findAllDrivers();
    List<Driver> findAvailableDrivers();
    Driver updateDriver(Driver driver);
    void deleteDriver(Long id);
    void assignVehicleToDriver(Driver driver, Vehicle vehicle);
    void updateDriverAvailability(Long driverId, boolean available);
    void updateDriverRating(Long driverId);
}
