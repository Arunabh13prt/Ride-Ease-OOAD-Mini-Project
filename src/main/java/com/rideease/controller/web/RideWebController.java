package com.rideease.controller.web;

import com.rideease.controller.DriverController;
import com.rideease.controller.RideController;
import com.rideease.controller.UserController;
import com.rideease.model.Driver;
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
    private final DriverController driverController;

    @Autowired
    public RideWebController(RideController rideController, UserController userController, 
                             DriverController driverController) {
        this.rideController = rideController;
        this.userController = userController;
        this.driverController = driverController;
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
            return "redirect:/rides";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel ride: " + e.getMessage());
            return "redirect:/web/rides/" + id;
        }
    }
    
    @GetMapping("/{id}/assign-driver")
    public String showAssignDriverForm(@PathVariable Long id, Model model) {
        Ride ride = rideController.getRideById(id);
        List<Driver> availableDrivers = driverController.getAvailableDrivers();
        
        model.addAttribute("ride", ride);
        model.addAttribute("drivers", availableDrivers);
        return "assign-driver";
    }
    
    @PostMapping("/{id}/assign-driver")
    public String assignDriver(
            @PathVariable Long id,
            @RequestParam("driverId") Long driverId,
            RedirectAttributes redirectAttributes) {
        
        try {
            Ride ride = rideController.assignDriverToRide(id, driverId);
            redirectAttributes.addFlashAttribute("successMessage", "Driver assigned successfully");
            return "redirect:/web/rides/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to assign driver: " + e.getMessage());
            return "redirect:/web/rides/" + id + "/assign-driver";
        }
    }
    
    @PostMapping("/{id}/start")
    public String startRide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Ride ride = rideController.startRide(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ride started successfully");
            return "redirect:/web/rides/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to start ride: " + e.getMessage());
            return "redirect:/web/rides/" + id;
        }
    }
    
    @PostMapping("/{id}/complete")
    public String completeRide(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Ride ride = rideController.completeRide(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ride completed successfully");
            return "redirect:/web/rides/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to complete ride: " + e.getMessage());
            return "redirect:/web/rides/" + id;
        }
    }
    
    @GetMapping("/{id}/rate")
    public String showRatingForm(@PathVariable Long id, Model model) {
        Ride ride = rideController.getRideById(id);
        model.addAttribute("ride", ride);
        return "rate-ride";
    }
    
    @PostMapping("/{id}/rate")
    public String rateRide(
            @PathVariable Long id,
            @RequestParam("rating") double rating,
            @RequestParam(value = "comment", required = false) String comment,
            RedirectAttributes redirectAttributes) {
        
        try {
            // This would call a RatingController method
            // ratingController.createRating(id, rating, comment);
            redirectAttributes.addFlashAttribute("successMessage", "Rating submitted successfully");
            return "redirect:/web/rides/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to submit rating: " + e.getMessage());
            return "redirect:/web/rides/" + id + "/rate";
        }
    }
}