package com.rideease.controller;

import com.rideease.model.Payment;
import com.rideease.model.Ride;
import com.rideease.model.enums.PaymentMethod;
import com.rideease.service.PaymentService;
import com.rideease.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for Payment-related operations
 * Part of MVC architecture: Acts as Controller
 * Team Member: Member 4
 */
@Component
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final RideService rideService;

    public Payment processPayment(Long rideId, PaymentMethod method) {
        // Get ride
        Ride ride = rideService.findRideById(rideId);
        
        // Process payment
        return paymentService.processPayment(ride, method);
    }

    public Payment getPaymentById(Long id) {
        return paymentService.findPaymentById(id);
    }

    public Payment getPaymentByRideId(Long rideId) {
        Ride ride = rideService.findRideById(rideId);
        return paymentService.findPaymentByRide(ride);
    }

    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentService.findPaymentsByUserId(userId);
    }

    public List<Payment> getPaymentsByDriverId(Long driverId) {
        return paymentService.findPaymentsByDriverId(driverId);
    }

    public List<Payment> getAllPayments() {
        return paymentService.findAllPayments();
    }

    public List<Payment> getCompletedPayments() {
        return paymentService.findCompletedPayments();
    }

    public List<Payment> getPendingPayments() {
        return paymentService.findPendingPayments();
    }
}
