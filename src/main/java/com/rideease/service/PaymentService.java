package com.rideease.service;

import com.rideease.model.Payment;
import com.rideease.model.Ride;
import com.rideease.model.enums.PaymentMethod;
import com.rideease.model.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
    Payment processPayment(Ride ride, PaymentMethod method, String cardDetails);
    Payment findPaymentById(Long id);
    Payment findPaymentByRide(Ride ride);
    List<Payment> findPaymentsByUserId(Long userId);
    List<Payment> findPaymentsByDriverId(Long driverId);
    List<Payment> findAllPayments();
    List<Payment> findCompletedPayments();
    List<Payment> findPendingPayments();
    String generateTransactionId();
}
