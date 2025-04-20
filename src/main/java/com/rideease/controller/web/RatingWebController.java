package com.rideease.controller.web;

import com.rideease.controller.DriverController;
import com.rideease.controller.RatingController;
import com.rideease.controller.RideController;
import com.rideease.model.Driver;
import com.rideease.model.Location;
import com.rideease.model.Rating;
import com.rideease.model.Ride;
import com.rideease.model.enums.RideStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Web controller for handling rating-related operations in browser
 */
@Controller
@RequestMapping("/web/ratings")
public class RatingWebController {

    private final RatingController ratingController;
    private final RideController rideController;
    private final DriverController driverController;

    @Autowired
    public RatingWebController(RatingController ratingController, RideController rideController, DriverController driverController) {
        this.ratingController = ratingController;
        this.rideController = rideController;
        this.driverController = driverController;
    }

    /**
     * Show rating form for a specific ride
     */
    @GetMapping("/{rideId}/rate")
    public String showRatingForm(@PathVariable Long rideId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Ride ride = rideController.getRideById(rideId);
            model.addAttribute("ride", ride);
            return "rating";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading rating form: " + e.getMessage());
            return "redirect:/rides";
        }
    }

    /**
     * Process rating for a ride
     */
    @PostMapping("/{rideId}/rate")
    public String processRating(
            @PathVariable Long rideId,
            @RequestParam("rating") int ratingValue,
            @RequestParam(value = "comment", required = false) String comment,
            RedirectAttributes redirectAttributes) {

        try {
            Rating rating = ratingController.addRating(rideId, ratingValue, comment);
            redirectAttributes.addFlashAttribute("successMessage", "Rating submitted successfully!");
            return "redirect:/rides";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rating submission error: " + e.getMessage());
            return "redirect:/web/ratings/" + rideId + "/rate";
        }
    }

    /**
     * Show rating form for a specific driver's most recent completed ride
     * If no completed rides are found, it will use the most recent ride of any status
     */
    @GetMapping("/driver/{driverId}/rate")
    public String showDriverRatingForm(@PathVariable Long driverId, Model model, RedirectAttributes redirectAttributes) {
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
            return "rating";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading rating form: " + e.getMessage());
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
     * Process rating for a driver's ride
     */
    @PostMapping("/driver/{driverId}/rate")
    public String processDriverRating(
            @PathVariable Long driverId,
            @RequestParam("rideId") Long rideId,
            @RequestParam("rating") int ratingValue,
            @RequestParam(value = "comment", required = false) String comment,
            RedirectAttributes redirectAttributes) {

        try {
            // Check if this is a test ride (ID 999)
            if (rideId == 999L) {
                // This is a test ride, just show success message
                redirectAttributes.addFlashAttribute("successMessage", "Test rating submitted successfully!");
                return "redirect:/drivers";
            }
            
            Rating rating = ratingController.addRating(rideId, ratingValue, comment);
            redirectAttributes.addFlashAttribute("successMessage", "Rating submitted successfully!");
            return "redirect:/drivers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rating submission error: " + e.getMessage());
            return "redirect:/web/ratings/driver/" + driverId + "/rate";
        }
    }
}
