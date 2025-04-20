package com.rideease.exception;

/**
 * Exception thrown when a ride cannot be found
 */
public class RideNotFoundException extends RuntimeException {
    
    public RideNotFoundException(String message) {
        super(message);
    }
    
    public RideNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
