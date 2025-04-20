package com.rideease.service;

import com.rideease.model.Vehicle;
import com.rideease.model.enums.VehicleType;

import java.util.List;

public interface VehicleService {
    Vehicle registerVehicle(Vehicle vehicle);
    Vehicle findVehicleById(Long id);
    Vehicle findVehicleByRegistrationNumber(String registrationNumber);
    List<Vehicle> findAllVehicles();
    List<Vehicle> findVehiclesByType(VehicleType type);
    Vehicle updateVehicle(Vehicle vehicle);
    void deleteVehicle(Long id);
    boolean isRegistrationNumberUnique(String registrationNumber);
}
