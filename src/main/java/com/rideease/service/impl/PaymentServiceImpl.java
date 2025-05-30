package com.rideease.service.impl;

import com.rideease.exception.PaymentFailedException;
import com.rideease.model.Payment;
import com.rideease.model.Ride;
import com.rideease.model.enums.PaymentMethod;
import com.rideease.model.enums.PaymentStatus;
import com.rideease.model.enums.RideStatus;
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
    public Payment processPayment(Ride ride, PaymentMethod method, String cardDetails) {
        // Check if payment already exists
        Payment existingPayment = paymentRepository.findByRide(ride).orElse(null);
        
        if (existingPayment != null && existingPayment.isCompleted()) {
            throw new IllegalStateException("Payment already completed for this ride");
        }
        
        Payment payment;
        if (existingPayment != null) {
            payment = existingPayment;
            // Update payment details if they've changed
            payment.setMethod(method);
            payment.setStatus(PaymentStatus.PENDING);
        } else {
            payment = Payment.builder()
                    .ride(ride)
                    .amount(ride.getFare())
                    .method(method)
                    .status(PaymentStatus.PENDING)
                    .completed(false)
                    .transactionId(generateTransactionId())
                    .build();
        }
        
        // Save the payment first in PENDING state
        payment = paymentRepository.save(payment);
        
        // Process payment (in a real system, this would integrate with payment gateway)
        try {
            // In a real implementation, we would use the cardDetails to process the payment
            // with a payment gateway API
            
            // Validate card details (simple validation for demo purposes)
            if (cardDetails == null || cardDetails.trim().isEmpty()) {
                throw new PaymentFailedException("Invalid card details");
            }
            
            // Simulate successful payment processing
            payment.setCompleted(true);
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaymentTime(LocalDateTime.now());
            
            // Update ride status if needed
            if (ride.getStatus() == RideStatus.COMPLETED) {
                // Payment is for a completed ride
                // No need to update ride status
            }
            
            return paymentRepository.save(payment);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setCompleted(false);
            paymentRepository.save(payment);
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
