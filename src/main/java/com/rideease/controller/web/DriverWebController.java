package com.rideease.controller.web;

import com.rideease.controller.DriverController;
import com.rideease.controller.VehicleController;
import com.rideease.model.Driver;
import com.rideease.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Web controller for handling driver-related operations in browser
 */
@Controller
@RequestMapping("/web/drivers")
public class DriverWebController {

    private final DriverController driverController;
    private final VehicleController vehicleController;

    @Autowired
    public DriverWebController(DriverController driverController, VehicleController vehicleController) {
        this.driverController = driverController;
        this.vehicleController = vehicleController;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Get all available vehicles to show in dropdown
        List<Vehicle> availableVehicles = vehicleController.getAvailableVehicles();
        model.addAttribute("vehicles", availableVehicles);
        return "register-driver";
    }

    @PostMapping("/register")
    public String registerDriver(
            @RequestParam("name") String name,
            @RequestParam("licenseNumber") String licenseNumber,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "vehicleId", required = false) Long vehicleId,
            RedirectAttributes redirectAttributes) {

        try {
            Driver driver = driverController.registerDriver(name, licenseNumber, phoneNumber);
            
            // Assign vehicle if provided
            if (vehicleId != null) {
                driverController.assignVehicle(driver.getId(), vehicleId);
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Driver registered successfully!");
            return "redirect:/drivers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to register driver: " + e.getMessage());
            return "redirect:/web/drivers/register";
        }
    }

    @GetMapping("/{id}")
    public String viewDriver(@PathVariable Long id, Model model) {
        Driver driver = driverController.getDriverById(id);
        model.addAttribute("driver", driver);
        return "driver-details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Driver driver = driverController.getDriverById(id);
        List<Vehicle> availableVehicles = vehicleController.getAvailableVehicles();
        model.addAttribute("driver", driver);
        model.addAttribute("vehicles", availableVehicles);
        return "edit-driver";
    }

    @PostMapping("/{id}/edit")
    public String updateDriver(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("licenseNumber") String licenseNumber,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "vehicleId", required = false) Long vehicleId,
            RedirectAttributes redirectAttributes) {

        try {
            Driver driver = driverController.updateDriverProfile(id, name, licenseNumber, phoneNumber);
            
            // Update vehicle if provided
            if (vehicleId != null) {
                driverController.assignVehicle(driver.getId(), vehicleId);
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Driver updated successfully!");
            return "redirect:/drivers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update driver: " + e.getMessage());
            return "redirect:/web/drivers/" + id + "/edit";
        }
    }
    
    @GetMapping("/{id}/toggle-availability")
    public String toggleDriverAvailability(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Driver driver = driverController.getDriverById(id);
            driverController.updateDriverAvailability(id, !driver.isAvailable());
            
            String statusMessage = driver.isAvailable() ? "unavailable" : "available";
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Driver " + driver.getName() + " is now " + statusMessage);
            
            return "redirect:/drivers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update driver availability: " + e.getMessage());
            return "redirect:/drivers";
        }
    }
}