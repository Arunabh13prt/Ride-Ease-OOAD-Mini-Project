package com.rideease.controller.web;

import com.rideease.controller.PaymentController;
import com.rideease.controller.RideController;
import com.rideease.model.Payment;
import com.rideease.model.Ride;
import com.rideease.model.enums.PaymentMethod;
import com.rideease.model.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Web controller for handling payment-related operations in browser
 */
@Controller
@RequestMapping("/web/rides")
public class PaymentWebController {

    private final PaymentController paymentController;
    private final RideController rideController;

    @Autowired
    public PaymentWebController(PaymentController paymentController, RideController rideController) {
        this.paymentController = paymentController;
        this.rideController = rideController;
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
}