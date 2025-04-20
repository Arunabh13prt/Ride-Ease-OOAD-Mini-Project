package com.rideease.controller.web;

import com.rideease.controller.DriverController;
import com.rideease.controller.PaymentController;
import com.rideease.controller.RideController;
import com.rideease.model.Driver;
import com.rideease.model.Location;
import com.rideease.model.Payment;
import com.rideease.model.Ride;
import com.rideease.model.enums.PaymentMethod;
import com.rideease.model.enums.PaymentStatus;
import com.rideease.model.enums.RideStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Web controller for handling payment-related operations in browser
 */
@Controller
@RequestMapping("/web/rides")
public class PaymentWebController {

    private final PaymentController paymentController;
    private final RideController rideController;
    private final DriverController driverController;

    @Autowired
    public PaymentWebController(PaymentController paymentController, RideController rideController, DriverController driverController) {
        this.paymentController = paymentController;
        this.rideController = rideController;
        this.driverController = driverController;
    }

    @GetMapping("/{rideId}/pay")
    public String showPaymentForm(@PathVariable Long rideId, Model model) {
        Ride ride = rideController.getRideById(rideId);
        model.addAttribute("ride", ride);
        return "payment";
    }

    @PostMapping("/{rideId}/pay")
    public String processPayment(
            @PathVariable Long rideId,
            @RequestParam("cardHolder") String cardHolder,
            @RequestParam("cardNumber") String cardNumber,
            @RequestParam("expiryDate") String expiryDate,
            @RequestParam("cvv") String cvv,
            @RequestParam("paymentMethod") PaymentMethod paymentMethod,
            @RequestParam(value = "saveCard", required = false) Boolean saveCard,
            RedirectAttributes redirectAttributes) {

        try {
            // Mask card number for security (only store last 4 digits)
            String maskedCardNumber = "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.length() - 4);
            
            // Process payment
            Payment payment = paymentController.processPayment(
                    rideId,
                    paymentMethod,
                    maskedCardNumber
            );
            
            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Payment failed. Please try again.");
            }
            
            return "redirect:/web/rides/" + rideId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment processing error: " + e.getMessage());
            return "redirect:/web/rides/" + rideId + "/pay";
        }
    }
    
    @GetMapping("/payments")
    public String viewAllPayments(Model model) {
        model.addAttribute("payments", paymentController.getAllPayments());
        return "payments";
    }
    
    @GetMapping("/payments/{paymentId}")
    public String viewPaymentDetails(@PathVariable Long paymentId, Model model) {
        Payment payment = paymentController.getPaymentById(paymentId);
        model.addAttribute("payment", payment);
        return "payment-details";
    }
    
    /**
     * Show payment form for a specific driver's most recent completed ride
     * If no completed rides are found, it will use the most recent ride of any status
     */
    @GetMapping("/driver/{driverId}/pay")
    public String showDriverPaymentForm(@PathVariable Long driverId, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Get the driver
            Driver driver = driverController.getDriverById(driverId);
            model.addAttribute("driver", driver);
            
            // Find the driver's most recent completed ride
            List<Ride> completedRides = rideController.getRidesByDriverIdAndStatus(driverId, RideStatus.COMPLETED);
            
            Ride ride;
            if (completedRides.isEmpty()) {
                // If no completed rides, try to get any ride for this driver
                List<Ride> anyRides = rideController.getRidesByDriver(driverId);
                
                if (anyRides.isEmpty()) {
                    // Create a dummy ride for testing purposes
                    ride = createDummyRide(driver);
                } else {
                    // Use the most recent ride of any status
                    ride = anyRides.get(0);
                }
            } else {
                // Get the most recent completed ride
                ride = completedRides.get(0);
            }
            
            model.addAttribute("ride", ride);
            return "payment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading payment form: " + e.getMessage());
            return "redirect:/drivers";
        }
    }
    
    /**
     * Create a dummy ride for testing purposes
     */
    private Ride createDummyRide(Driver driver) {
        // Create a dummy ride with test data
        return Ride.builder()
                .id(999L) // Use a high ID that's unlikely to conflict
                .driver(driver)
                .status(RideStatus.COMPLETED)
                .fare(250.0)
                .distance(5.2)
                .pickupLocation(Location.builder().address("123 Test Street").build())
                .destinationLocation(Location.builder().address("456 Demo Avenue").build())
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Process payment for a driver's ride
     */
    @PostMapping("/driver/{driverId}/pay")
    public String processDriverPayment(
            @PathVariable Long driverId,
            @RequestParam("rideId") Long rideId,
            @RequestParam("cardHolder") String cardHolder,
            @RequestParam("cardNumber") String cardNumber,
            @RequestParam("expiryDate") String expiryDate,
            @RequestParam("cvv") String cvv,
            @RequestParam("paymentMethod") PaymentMethod paymentMethod,
            @RequestParam(value = "saveCard", required = false) Boolean saveCard,
            RedirectAttributes redirectAttributes) {

        try {
            // Check if this is a test ride (ID 999)
            if (rideId == 999L) {
                // This is a test ride, just show success message
                redirectAttributes.addFlashAttribute("successMessage", "Test payment processed successfully!");
                return "redirect:/drivers";
            }
            
            // Mask card number for security (only store last 4 digits)
            String maskedCardNumber = "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.length() - 4);
            
            // Process payment
            Payment payment = paymentController.processPayment(
                    rideId,
                    paymentMethod,
                    maskedCardNumber
            );
            
            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Payment failed. Please try again.");
            }
            
            return "redirect:/drivers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment processing error: " + e.getMessage());
            return "redirect:/web/rides/driver/" + driverId + "/pay";
        }
    }
}