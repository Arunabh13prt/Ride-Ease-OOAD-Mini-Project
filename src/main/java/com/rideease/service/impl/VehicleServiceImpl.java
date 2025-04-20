package com.rideease.service.impl;

import com.rideease.model.Vehicle;
import com.rideease.model.enums.VehicleType;
import com.rideease.repository.VehicleRepository;
import com.rideease.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of VehicleService interface
 * Follows Open/Closed Principle (OCP) from SOLID
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public Vehicle registerVehicle(Vehicle vehicle) {
        // Check if vehicle already exists
        if (vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
            throw new IllegalArgumentException("Vehicle with this registration number already exists");
        }
        
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with id: " + id));
    }

    @Override
    public Vehicle findVehicleByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with registration number: " + registrationNumber));
    }

    @Override
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findVehiclesByType(VehicleType type) {
        return vehicleRepository.findByType(type);
    }

    @Override
    public Vehicle updateVehicle(Vehicle vehicle) {
        // Check if vehicle exists
        Vehicle existingVehicle = findVehicleById(vehicle.getId());
        
        // Update vehicle details
        existingVehicle.setModel(vehicle.getModel());
        existingVehicle.setColor(vehicle.getColor());
        existingVehicle.setType(vehicle.getType());
        existingVehicle.setCapacity(vehicle.getCapacity());
        
        // Only update registration number if it's changed and unique
        if (!existingVehicle.getRegistrationNumber().equals(vehicle.getRegistrationNumber())) {
            if (vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
                throw new IllegalArgumentException("Vehicle with this registration number already exists");
            }
            existingVehicle.setRegistrationNumber(vehicle.getRegistrationNumber());
        }
        
        return vehicleRepository.save(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        // Check if vehicle exists
        Vehicle vehicle = findVehicleById(id);
        vehicleRepository.delete(vehicle);
    }

    @Override
    public boolean isRegistrationNumberUnique(String registrationNumber) {
        return !vehicleRepository.existsByRegistrationNumber(registrationNumber);
    }
}
