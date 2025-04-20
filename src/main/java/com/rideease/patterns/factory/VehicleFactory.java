package com.rideease.patterns.factory;

import com.rideease.model.Vehicle;
import com.rideease.model.enums.VehicleType;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern Implementation
 * Creates different types of vehicles based on vehicle type
 * Team Member: Member 2
 */
@Component
public class VehicleFactory {
    
    // Factory method to create vehicles of different types
    public Vehicle createVehicle(VehicleType type, String model, String registrationNumber, String color, int capacity) {
        Vehicle vehicle = Vehicle.builder()
                .model(model)
                .registrationNumber(registrationNumber)
                .type(type)
                .color(color)
                .build();
        
        // Set specific attributes based on vehicle type
        switch (type) {
            case SEDAN:
                vehicle.setCapacity(4);
                break;
            case SUV:
                vehicle.setCapacity(6);
                break;
            case HATCHBACK:
                vehicle.setCapacity(4);
                break;
            case LUXURY:
                vehicle.setCapacity(4);
                break;
            case AUTO_RICKSHAW:
                vehicle.setCapacity(3);
                break;
            default:
                // Use provided capacity
                vehicle.setCapacity(capacity);
        }
        
        return vehicle;
    }
}
