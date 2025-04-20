package com.rideease.controller.web;

import com.rideease.controller.RideController;
import com.rideease.controller.UserController;
import com.rideease.model.Location;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.RideStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Web controller for handling ride-related operations in browser
 */
@Controller
@RequestMapping("/web/rides")
public class RideWebController {

    private final RideController rideController;
    private final UserController userController;

    @Autowired
    public RideWebController(RideController rideController, UserController userController) {
        this.rideController = rideController;
        this.userController = userController;
    }

    @GetMapping("/book")
    public String showBookRideForm(Model model) {
        List<User> users = userController.findAllUsers();
        model.addAttribute("users", users);
        return "book-ride";
    }

    @PostMapping("/book")
    public String bookRide(
            @RequestParam("userId") Long userId,
            @RequestParam("pickupAddress") String pickupAddress,
            @RequestParam("pickupLatitude") double pickupLatitude,
            @RequestParam("pickupLongitude") double pickupLongitude,
            @RequestParam("destinationAddress") String destinationAddress,
            @RequestParam("destinationLatitude") double destinationLatitude,
            @RequestParam("destinationLongitude") double destinationLongitude,
            @RequestParam("pickupTime") String pickupTimeString,
            RedirectAttributes redirectAttributes) {

        try {
            // Parse pickup time
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime pickupTime = LocalDateTime.parse(pickupTimeString, formatter);
            
            // Create location objects
            Location pickupLocation = Location.builder()
                    .address(pickupAddress)
                    .latitude(pickupLatitude)
                    .longitude(pickupLongitude)
                    .build();
            
            Location destinationLocation = Location.builder()
                    .address(destinationAddress)
                    .latitude(destinationLatitude)
                    .longitude(destinationLongitude)
                    .build();
            
            // Book the ride using builder pattern
            Ride ride = rideController.bookRideUsingBuilder(userId, pickupLocation, destinationLocation, pickupTime);
            
            redirectAttributes.addFlashAttribute("successMessage", "Ride booked successfully! Ride ID: " + ride.getId());
            return "redirect:/rides"; // Redirect to rides list
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to book ride: " + e.getMessage());
            return "redirect:/web/rides/book";
        }
    }
    
    @GetMapping("/find")
    public String showFindRideForm() {
        return "find-ride";
    }
    
    @GetMapping("/{id}")
    public String viewRideDetails(@PathVariable Long id, Model model) {
        Ride ride = rideController.getRideById(id);
        model.addAttribute("ride", ride);
        return "ride-details";
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelRide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Ride ride = rideController.cancelRide(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ride cancelled successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel ride: " + e.getMessage());
        }
        return "redirect:/rides";
    }
}