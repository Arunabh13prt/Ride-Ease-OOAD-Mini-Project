package com.rideease.controller;

import com.rideease.model.Vehicle;
import com.rideease.model.enums.VehicleType;
import com.rideease.patterns.factory.VehicleFactory;
import com.rideease.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for Vehicle-related operations
 * Part of MVC architecture: Acts as Controller
 * Team Member: Member 2
 */
@Component
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleFactory vehicleFactory;

    @Autowired
    public VehicleController(VehicleService vehicleService, VehicleFactory vehicleFactory) {
        this.vehicleService = vehicleService;
        this.vehicleFactory = vehicleFactory;
    }

    public Vehicle registerVehicle(String model, String registrationNumber, VehicleType vehicleType, String color, int capacity) {
        // Create vehicle using factory pattern
        Vehicle vehicle = vehicleFactory.createVehicle(vehicleType, model, registrationNumber, color, capacity);
        return vehicleService.registerVehicle(vehicle);
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleService.findVehicleById(id);
    }

    public Vehicle getVehicleByRegistrationNumber(String registrationNumber) {
        return vehicleService.findVehicleByRegistrationNumber(registrationNumber);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleService.findAllVehicles();
    }
    
    public List<Vehicle> getVehiclesByType(VehicleType type) {
        return vehicleService.findVehiclesByType(type);
    }

    public List<Vehicle> getAvailableVehicles() {
        // This assumes Vehicles linked to drivers are not available for new assignments
        List<Vehicle> allVehicles = vehicleService.findAllVehicles();
        return allVehicles.stream()
                .filter(vehicle -> vehicle.getDriver() == null)
                .collect(Collectors.toList());
    }

    public Vehicle updateVehicle(Long id, String model, String registrationNumber, VehicleType vehicleType, String color, int capacity) {
        // Get existing vehicle
        Vehicle vehicle = vehicleService.findVehicleById(id);
        
        // Update fields
        vehicle.setModel(model);
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setType(vehicleType);
        vehicle.setColor(color);
        vehicle.setCapacity(capacity);
        
        return vehicleService.updateVehicle(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleService.deleteVehicle(id);
    }
    
    public boolean isRegistrationNumberUnique(String registrationNumber) {
        return vehicleService.isRegistrationNumberUnique(registrationNumber);
    }
}