package com.rideease.service.impl;

import com.rideease.exception.PaymentFailedException;
import com.rideease.model.Payment;
import com.rideease.model.Ride;
import com.rideease.model.enums.PaymentMethod;
import com.rideease.repository.PaymentRepository;
import com.rideease.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of PaymentService interface
 * Follows Interface Segregation Principle (ISP) from SOLID
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment processPayment(Ride ride, PaymentMethod method) {
        // Check if payment already exists
        Payment existingPayment = paymentRepository.findByRide(ride).orElse(null);
        
        if (existingPayment != null && existingPayment.isCompleted()) {
            throw new IllegalStateException("Payment already completed for this ride");
        }
        
        Payment payment;
        if (existingPayment != null) {
            payment = existingPayment;
        } else {
            payment = Payment.builder()
                    .ride(ride)
                    .amount(ride.getFare())
                    .method(method)
                    .completed(false)
                    .transactionId(generateTransactionId())
                    .build();
        }
        
        // Process payment (in a real system, this would integrate with payment gateway)
        try {
            // Simulate payment processing
            payment.setCompleted(true);
            payment.setPaymentTime(LocalDateTime.now());
            
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new PaymentFailedException("Payment processing failed: " + e.getMessage());
        }
    }

    @Override
    public Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));
    }

    @Override
    public Payment findPaymentByRide(Ride ride) {
        return paymentRepository.findByRide(ride)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for ride: " + ride.getId()));
    }

    @Override
    public List<Payment> findPaymentsByUserId(Long userId) {
        return paymentRepository.findByRide_User_Id(userId);
    }

    @Override
    public List<Payment> findPaymentsByDriverId(Long driverId) {
        return paymentRepository.findByRide_Driver_Id(driverId);
    }

    @Override
    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> findCompletedPayments() {
        return paymentRepository.findByCompletedTrue();
    }

    @Override
    public List<Payment> findPendingPayments() {
        return paymentRepository.findByCompletedFalse();
    }

    @Override
    public String generateTransactionId() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
