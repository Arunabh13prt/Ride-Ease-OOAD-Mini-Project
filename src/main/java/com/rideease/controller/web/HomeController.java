package com.rideease.controller.web;

import com.rideease.model.User;
import com.rideease.service.DriverService;
import com.rideease.service.RideService;
import com.rideease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Web controller for handling browser-based interactions
 */
@Controller
public class HomeController {

    private final UserService userService;
    private final DriverService driverService;
    private final RideService rideService;

    @Autowired
    public HomeController(UserService userService, DriverService driverService, RideService rideService) {
        this.userService = userService;
        this.driverService = driverService;
        this.rideService = rideService;
    }

    @GetMapping("/")
    public String home(Model model, RedirectAttributes redirectAttributes) {
        // Add counts for dashboard
        model.addAttribute("userCount", userService.findAllUsers().size());
        model.addAttribute("driverCount", driverService.findAllDrivers().size());
        model.addAttribute("rideCount", rideService.findAllRides().size());
        
        // Add current user if logged in
        try {
            User currentUser = userService.getCurrentLoggedInUser();
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
            }
        } catch (Exception e) {
            // User not logged in, no need to do anything
        }
        
        return "home";
    }
    
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        
        // Add current user if logged in
        try {
            User currentUser = userService.getCurrentLoggedInUser();
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
            }
        } catch (Exception e) {
            // User not logged in, no need to do anything
        }
        
        return "users";
    }
    
    @GetMapping("/drivers")
    public String drivers(Model model) {
        model.addAttribute("drivers", driverService.findAllDrivers());
        
        // Add current user if logged in
        try {
            User currentUser = userService.getCurrentLoggedInUser();
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
            }
        } catch (Exception e) {
            // User not logged in, no need to do anything
        }
        
        return "drivers";
    }
    
    @GetMapping("/rides")
    public String rides(Model model) {
        model.addAttribute("rides", rideService.findAllRides());
        
        // Add current user if logged in
        try {
            User currentUser = userService.getCurrentLoggedInUser();
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
            }
        } catch (Exception e) {
            // User not logged in, no need to do anything
        }
        
        return "rides";
    }
}