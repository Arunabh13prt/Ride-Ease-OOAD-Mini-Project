package com.rideease.cli;

import com.rideease.controller.DriverController;
import com.rideease.controller.PaymentController;
import com.rideease.controller.RideController;
import com.rideease.controller.UserController;
import com.rideease.model.Driver;
import com.rideease.model.Location;
import com.rideease.model.Ride;
import com.rideease.model.User;
import com.rideease.model.enums.PaymentMethod;
import com.rideease.model.enums.RideStatus;
import com.rideease.model.enums.VehicleType;
import com.rideease.patterns.command.BookRideCommand;
import com.rideease.patterns.command.CancelRideCommand;
import com.rideease.patterns.command.Command;
import com.rideease.patterns.command.CommandInvoker;
import com.rideease.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class RideEaseCLI {

    private final Scanner scanner;
    private final UserController userController;
    private final DriverController driverController;
    private final RideController rideController;
    private final PaymentController paymentController;
    private final RatingService ratingService;
    private final CommandInvoker commandInvoker;
    
    private boolean isRunning = true;

    public void start() {
        System.out.println("Welcome to RideEase - Ride Booking System");
        
        while (isRunning) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice");
            
            try {
                processMainMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        System.out.println("Thank you for using RideEase!");
    }

    private void displayMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. User Menu");
        System.out.println("2. Driver Menu");
        System.out.println("3. Ride Menu");
        System.out.println("4. Payment Menu");
        System.out.println("5. Rating Menu");
        System.out.println("6. Admin Menu");
        System.out.println("0. Exit");
        System.out.println("=====================");
    }

    private void processMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                displayUserMenu();
                break;
            case 2:
                displayDriverMenu();
                break;
            case 3:
                displayRideMenu();
                break;
            case 4:
                displayPaymentMenu();
                break;
            case 5:
                displayRatingMenu();
                break;
            case 6:
                displayAdminMenu();
                break;
            case 0:
                isRunning = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // User Menu
    private void displayUserMenu() {
        boolean userMenuRunning = true;
        
        while (userMenuRunning) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. Register User");
            System.out.println("2. Login User");
            System.out.println("3. Logout User");
            System.out.println("4. View Profile");
            System.out.println("5. Update Profile");
            System.out.println("0. Back to Main Menu");
            System.out.println("=====================");
            
            int choice = getIntInput("Enter your choice");
            
            try {
                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        loginUser();
                        break;
                    case 3:
                        logoutUser();
                        break;
                    case 4:
                        viewUserProfile();
                        break;
                    case 5:
                        updateUserProfile();
                        break;
                    case 0:
                        userMenuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registerUser() {
        System.out.println("\n=== Register User ===");
        String name = getStringInput("Enter name");
        String email = getStringInput("Enter email");
        String password = getStringInput("Enter password");
        String phoneNumber = getStringInput("Enter phone number");
        
        User user = userController.registerUser(name, email, password, phoneNumber);
        System.out.println("User registered successfully: " + user);
    }

    private void loginUser() {
        System.out.println("\n=== Login User ===");
        String email = getStringInput("Enter email");
        String password = getStringInput("Enter password");
        
        boolean success = userController.loginUser(email, password);
        if (success) {
            User user = userController.getCurrentUser();
            System.out.println("Login successful. Welcome, " + user.getName() + "!");
        } else {
            System.out.println("Login failed. Invalid email or password.");
        }
    }

    private void logoutUser() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            userController.logoutUser();
            System.out.println("Logout successful. Goodbye, " + currentUser.getName() + "!");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    private void viewUserProfile() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            System.out.println("\n=== User Profile ===");
            System.out.println("ID: " + currentUser.getId());
            System.out.println("Name: " + currentUser.getName());
            System.out.println("Email: " + currentUser.getEmail());
            System.out.println("Phone: " + currentUser.getPhoneNumber());
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    private void updateUserProfile() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            System.out.println("\n=== Update Profile ===");
            String name = getStringInput("Enter new name (or press Enter to keep current)");
            String email = getStringInput("Enter new email (or press Enter to keep current)");
            String phoneNumber = getStringInput("Enter new phone number (or press Enter to keep current)");
            String password = getStringInput("Enter new password (or press Enter to keep current)");
            
            // Use current values if new ones are not provided
            name = name.isEmpty() ? currentUser.getName() : name;
            email = email.isEmpty() ? currentUser.getEmail() : email;
            phoneNumber = phoneNumber.isEmpty() ? currentUser.getPhoneNumber() : phoneNumber;
            
            User updatedUser = userController.updateUserProfile(currentUser.getId(), name, email, phoneNumber, password);
            System.out.println("Profile updated successfully: " + updatedUser);
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    // Driver Menu
    private void displayDriverMenu() {
        boolean driverMenuRunning = true;
        
        while (driverMenuRunning) {
            System.out.println("\n===== DRIVER MENU =====");
            System.out.println("1. Register Driver");
            System.out.println("2. View All Drivers");
            System.out.println("3. View Available Drivers");
            System.out.println("4. View Driver Details");
            System.out.println("5. Update Driver Availability");
            System.out.println("0. Back to Main Menu");
            System.out.println("========================");
            
            int choice = getIntInput("Enter your choice");
            
            try {
                switch (choice) {
                    case 1:
                        registerDriver();
                        break;
                    case 2:
                        viewAllDrivers();
                        break;
                    case 3:
                        viewAvailableDrivers();
                        break;
                    case 4:
                        viewDriverDetails();
                        break;
                    case 5:
                        updateDriverAvailability();
                        break;
                    case 0:
                        driverMenuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registerDriver() {
        System.out.println("\n=== Register Driver ===");
        String name = getStringInput("Enter name");
        String phoneNumber = getStringInput("Enter phone number");
        String licenseNumber = getStringInput("Enter license number");
        
        // Vehicle details
        String vehicleModel = getStringInput("Enter vehicle model");
        String registrationNumber = getStringInput("Enter vehicle registration number");
        String color = getStringInput("Enter vehicle color");
        int capacity = getIntInput("Enter vehicle capacity");
        
        // Vehicle type selection
        System.out.println("Select vehicle type:");
        for (VehicleType type : VehicleType.values()) {
            System.out.println(type.ordinal() + ". " + type);
        }
        int typeChoice = getIntInput("Enter vehicle type number");
        VehicleType vehicleType = VehicleType.values()[typeChoice];
        
        Driver driver = driverController.registerDriver(name, phoneNumber, licenseNumber, 
                                                      vehicleModel, registrationNumber, 
                                                      vehicleType, color, capacity);
        System.out.println("Driver registered successfully: " + driver);
    }

    private void viewAllDrivers() {
        System.out.println("\n=== All Drivers ===");
        List<Driver> drivers = driverController.getAllDrivers();
        
        if (drivers.isEmpty()) {
            System.out.println("No drivers found.");
        } else {
            for (Driver driver : drivers) {
                System.out.println(driver);
            }
        }
    }

    private void viewAvailableDrivers() {
        System.out.println("\n=== Available Drivers ===");
        List<Driver> drivers = driverController.getAvailableDrivers();
        
        if (drivers.isEmpty()) {
            System.out.println("No available drivers found.");
        } else {
            for (Driver driver : drivers) {
                System.out.println(driver);
            }
        }
    }

    private void viewDriverDetails() {
        System.out.println("\n=== Driver Details ===");
        Long driverId = getLongInput("Enter driver ID");
        
        Driver driver = driverController.getDriverById(driverId);
        System.out.println(driver);
        System.out.println("Vehicle: " + driver.getVehicle());
    }

    private void updateDriverAvailability() {
        System.out.println("\n=== Update Driver Availability ===");
        Long driverId = getLongInput("Enter driver ID");
        boolean available = getBooleanInput("Is driver available? (true/false)");
        
        driverController.updateDriverAvailability(driverId, available);
        System.out.println("Driver availability updated successfully.");
    }

    // Ride Menu
    private void displayRideMenu() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to access the ride menu.");
            return;
        }
        
        boolean rideMenuRunning = true;
        
        while (rideMenuRunning) {
            System.out.println("\n===== RIDE MENU =====");
            System.out.println("1. Book a Ride");
            System.out.println("2. Book a Ride (Using Command Pattern)");
            System.out.println("3. View My Rides");
            System.out.println("4. View Ride Details");
            System.out.println("5. Cancel a Ride");
            System.out.println("6. Cancel a Ride (Using Command Pattern)");
            System.out.println("0. Back to Main Menu");
            System.out.println("=====================");
            
            int choice = getIntInput("Enter your choice");
            
            try {
                switch (choice) {
                    case 1:
                        bookRide();
                        break;
                    case 2:
                        bookRideWithCommandPattern();
                        break;
                    case 3:
                        viewMyRides();
                        break;
                    case 4:
                        viewRideDetails();
                        break;
                    case 5:
                        cancelRide();
                        break;
                    case 6:
                        cancelRideWithCommandPattern();
                        break;
                    case 0:
                        rideMenuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void bookRide() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to book a ride.");
            return;
        }
        
        System.out.println("\n=== Book a Ride ===");
        
        // Get pickup location
        System.out.println("Pickup Location:");
        double pickupLatitude = getDoubleInput("Enter pickup latitude");
        double pickupLongitude = getDoubleInput("Enter pickup longitude");
        String pickupAddress = getStringInput("Enter pickup address");
        
        // Get destination location
        System.out.println("Destination Location:");
        double destLatitude = getDoubleInput("Enter destination latitude");
        double destLongitude = getDoubleInput("Enter destination longitude");
        String destAddress = getStringInput("Enter destination address");
        
        // Get pickup time
        LocalDateTime pickupTime = getDateTimeInput("Enter pickup time (yyyy-MM-dd HH:mm)");
        
        // Create location objects
        Location pickup = new Location(pickupLatitude, pickupLongitude, pickupAddress);
        Location destination = new Location(destLatitude, destLongitude, destAddress);
        
        // Book ride
        Ride ride = rideController.bookRide(currentUser.getId(), pickup, destination, pickupTime);
        System.out.println("Ride booked successfully: " + ride);
        
        // Find available drivers
        List<Driver> availableDrivers = driverController.getAvailableDrivers();
        if (!availableDrivers.isEmpty()) {
            System.out.println("\nAvailable drivers:");
            for (int i = 0; i < availableDrivers.size(); i++) {
                System.out.println((i + 1) + ". " + availableDrivers.get(i));
            }
            
            int driverIndex = getIntInput("Select a driver (1-" + availableDrivers.size() + ")") - 1;
            if (driverIndex >= 0 && driverIndex < availableDrivers.size()) {
                Driver selectedDriver = availableDrivers.get(driverIndex);
                Ride updatedRide = rideController.assignDriverToRide(ride.getId(), selectedDriver.getId());
                System.out.println("Driver assigned: " + updatedRide.getDriver().getName());
            } else {
                System.out.println("Invalid driver selection.");
            }
        } else {
            System.out.println("No drivers available at the moment.");
        }
    }
    
    private void bookRideWithCommandPattern() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to book a ride.");
            return;
        }
        
        System.out.println("\n=== Book a Ride (Command Pattern) ===");
        
        // Get pickup location
        System.out.println("Pickup Location:");
        double pickupLatitude = getDoubleInput("Enter pickup latitude");
        double pickupLongitude = getDoubleInput("Enter pickup longitude");
        String pickupAddress = getStringInput("Enter pickup address");
        
        // Get destination location
        System.out.println("Destination Location:");
        double destLatitude = getDoubleInput("Enter destination latitude");
        double destLongitude = getDoubleInput("Enter destination longitude");
        String destAddress = getStringInput("Enter destination address");
        
        // Get pickup time
        LocalDateTime pickupTime = getDateTimeInput("Enter pickup time (yyyy-MM-dd HH:mm)");
        
        // Create location objects
        Location pickup = new Location(pickupLatitude, pickupLongitude, pickupAddress);
        Location destination = new Location(destLatitude, destLongitude, destAddress);
        
        // Create book ride command
        Command bookRideCommand = new BookRideCommand(rideController, currentUser.getId(), pickup, destination, pickupTime);
        
        // Execute command
        Ride ride = (Ride) commandInvoker.executeCommand(bookRideCommand);
        System.out.println("Ride booked successfully using Command Pattern: " + ride);
    }

    private void viewMyRides() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to view your rides.");
            return;
        }
        
        System.out.println("\n=== My Rides ===");
        List<Ride> rides = rideController.getRidesByUser(currentUser.getId());
        
        if (rides.isEmpty()) {
            System.out.println("No rides found.");
        } else {
            for (Ride ride : rides) {
                System.out.println(ride);
            }
        }
    }

    private void viewRideDetails() {
        System.out.println("\n=== Ride Details ===");
        Long rideId = getLongInput("Enter ride ID");
        
        Ride ride = rideController.getRideById(rideId);
        System.out.println("Ride Details:");
        System.out.println(ride);
        System.out.println("User: " + ride.getUser().getName());
        System.out.println("Driver: " + (ride.getDriver() != null ? ride.getDriver().getName() : "Not assigned"));
        System.out.println("Status: " + ride.getStatus());
        System.out.println("Fare: $" + ride.getFare());
        System.out.println("Distance: " + ride.getDistance() + " km");
        System.out.println("Pickup: " + ride.getPickupLocation());
        System.out.println("Destination: " + ride.getDestinationLocation());
        System.out.println("Pickup Time: " + ride.getPickupTime());
        System.out.println("Drop-off Time: " + (ride.getDropOffTime() != null ? ride.getDropOffTime() : "Not completed"));
    }

    private void cancelRide() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to cancel a ride.");
            return;
        }
        
        System.out.println("\n=== Cancel a Ride ===");
        
        // Show user rides
        List<Ride> rides = rideController.getRidesByUser(currentUser.getId());
        if (rides.isEmpty()) {
            System.out.println("No rides found.");
            return;
        }
        
        System.out.println("Your rides:");
        for (Ride ride : rides) {
            if (ride.getStatus() != RideStatus.COMPLETED && ride.getStatus() != RideStatus.CANCELLED) {
                System.out.println("ID: " + ride.getId() + " - Status: " + ride.getStatus() + 
                                   " - From: " + ride.getPickupLocation().getAddress() + 
                                   " - To: " + ride.getDestinationLocation().getAddress());
            }
        }
        
        Long rideId = getLongInput("Enter the ID of the ride to cancel");
        Ride cancelledRide = rideController.cancelRide(rideId);
        System.out.println("Ride cancelled successfully: " + cancelledRide);
    }
    
    private void cancelRideWithCommandPattern() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to cancel a ride.");
            return;
        }
        
        System.out.println("\n=== Cancel a Ride (Command Pattern) ===");
        
        // Show user rides
        List<Ride> rides = rideController.getRidesByUser(currentUser.getId());
        if (rides.isEmpty()) {
            System.out.println("No rides found.");
            return;
        }
        
        System.out.println("Your rides:");
        for (Ride ride : rides) {
            if (ride.getStatus() != RideStatus.COMPLETED && ride.getStatus() != RideStatus.CANCELLED) {
                System.out.println("ID: " + ride.getId() + " - Status: " + ride.getStatus() + 
                                   " - From: " + ride.getPickupLocation().getAddress() + 
                                   " - To: " + ride.getDestinationLocation().getAddress());
            }
        }
        
        Long rideId = getLongInput("Enter the ID of the ride to cancel");
        
        // Create cancel ride command
        Command cancelRideCommand = new CancelRideCommand(rideController, rideId);
        
        // Execute command
        Ride cancelledRide = (Ride) commandInvoker.executeCommand(cancelRideCommand);
        System.out.println("Ride cancelled successfully using Command Pattern: " + cancelledRide);
    }

    // Payment Menu
    private void displayPaymentMenu() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to access the payment menu.");
            return;
        }
        
        boolean paymentMenuRunning = true;
        
        while (paymentMenuRunning) {
            System.out.println("\n===== PAYMENT MENU =====");
            System.out.println("1. Process Payment for a Ride");
            System.out.println("2. View My Payments");
            System.out.println("3. View Payment Details");
            System.out.println("0. Back to Main Menu");
            System.out.println("========================");
            
            int choice = getIntInput("Enter your choice");
            
            try {
                switch (choice) {
                    case 1:
                        processPayment();
                        break;
                    case 2:
                        viewMyPayments();
                        break;
                    case 3:
                        viewPaymentDetails();
                        break;
                    case 0:
                        paymentMenuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void processPayment() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to process a payment.");
            return;
        }
        
        System.out.println("\n=== Process Payment ===");
        
        // Show completed rides without payment
        List<Ride> rides = rideController.getRidesByUser(currentUser.getId());
        boolean hasRidesToPay = false;
        
        System.out.println("Completed rides that need payment:");
        for (Ride ride : rides) {
            if (ride.getStatus() == RideStatus.COMPLETED) {
                try {
                    paymentController.getPaymentByRideId(ride.getId());
                } catch (Exception e) {
                    System.out.println("ID: " + ride.getId() + 
                                       " - From: " + ride.getPickupLocation().getAddress() + 
                                       " - To: " + ride.getDestinationLocation().getAddress() + 
                                       " - Fare: $" + ride.getFare());
                    hasRidesToPay = true;
                }
            }
        }
        
        if (!hasRidesToPay) {
            System.out.println("No rides need payment.");
            return;
        }
        
        Long rideId = getLongInput("Enter the ID of the ride to pay for");
        
        // Select payment method
        System.out.println("Select payment method:");
        for (PaymentMethod method : PaymentMethod.values()) {
            System.out.println(method.ordinal() + ". " + method);
        }
        int methodChoice = getIntInput("Enter payment method number");
        PaymentMethod paymentMethod = PaymentMethod.values()[methodChoice];
        
        // Process payment
        try {
            paymentController.processPayment(rideId, paymentMethod);
            System.out.println("Payment processed successfully.");
        } catch (Exception e) {
            System.out.println("Payment failed: " + e.getMessage());
        }
    }

    private void viewMyPayments() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to view your payments.");
            return;
        }
        
        System.out.println("\n=== My Payments ===");
        try {
            List<com.rideease.model.Payment> payments = paymentController.getPaymentsByUserId(currentUser.getId());
            
            if (payments.isEmpty()) {
                System.out.println("No payments found.");
            } else {
                for (com.rideease.model.Payment payment : payments) {
                    System.out.println("ID: " + payment.getId() + 
                                       " - Ride ID: " + payment.getRide().getId() + 
                                       " - Amount: $" + payment.getAmount() + 
                                       " - Method: " + payment.getMethod() + 
                                       " - Status: " + (payment.isCompleted() ? "Completed" : "Pending"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving payments: " + e.getMessage());
        }
    }

    private void viewPaymentDetails() {
        System.out.println("\n=== Payment Details ===");
        Long paymentId = getLongInput("Enter payment ID");
        
        try {
            com.rideease.model.Payment payment = paymentController.getPaymentById(paymentId);
            System.out.println("Payment Details:");
            System.out.println("ID: " + payment.getId());
            System.out.println("Ride ID: " + payment.getRide().getId());
            System.out.println("Amount: $" + payment.getAmount());
            System.out.println("Method: " + payment.getMethod());
            System.out.println("Transaction ID: " + payment.getTransactionId());
            System.out.println("Status: " + (payment.isCompleted() ? "Completed" : "Pending"));
            System.out.println("Payment Time: " + payment.getPaymentTime());
        } catch (Exception e) {
            System.out.println("Error retrieving payment: " + e.getMessage());
        }
    }

    // Rating Menu
    private void displayRatingMenu() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to access the rating menu.");
            return;
        }
        
        boolean ratingMenuRunning = true;
        
        while (ratingMenuRunning) {
            System.out.println("\n===== RATING MENU =====");
            System.out.println("1. Rate a Driver");
            System.out.println("2. View My Ratings");
            System.out.println("0. Back to Main Menu");
            System.out.println("======================");
            
            int choice = getIntInput("Enter your choice");
            
            try {
                switch (choice) {
                    case 1:
                        rateDriver();
                        break;
                    case 2:
                        viewMyRatings();
                        break;
                    case 0:
                        ratingMenuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void rateDriver() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to rate a driver.");
            return;
        }
        
        System.out.println("\n=== Rate a Driver ===");
        
        // Show completed rides without rating
        List<Ride> rides = rideController.getRidesByUser(currentUser.getId());
        boolean hasRidesToRate = false;
        
        System.out.println("Completed rides that can be rated:");
        for (Ride ride : rides) {
            if (ride.getStatus() == RideStatus.COMPLETED) {
                try {
                    ratingService.findRatingByRide(ride);
                } catch (Exception e) {
                    System.out.println("ID: " + ride.getId() + 
                                       " - Driver: " + ride.getDriver().getName() + 
                                       " - From: " + ride.getPickupLocation().getAddress() + 
                                       " - To: " + ride.getDestinationLocation().getAddress());
                    hasRidesToRate = true;
                }
            }
        }
        
        if (!hasRidesToRate) {
            System.out.println("No rides to rate.");
            return;
        }
        
        Long rideId = getLongInput("Enter the ID of the ride to rate");
        int rating = getIntInput("Enter rating (1-5)");
        while (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5");
            rating = getIntInput("Enter rating (1-5)");
        }
        
        String comment = getStringInput("Enter comment (optional)");
        
        try {
            Ride ride = rideController.getRideById(rideId);
            ratingService.addUserRating(ride, rating, comment);
            System.out.println("Rating submitted successfully.");
        } catch (Exception e) {
            System.out.println("Rating submission failed: " + e.getMessage());
        }
    }

    private void viewMyRatings() {
        User currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("You need to login first to view your ratings.");
            return;
        }
        
        System.out.println("\n=== My Ratings ===");
        try {
            List<Ride> rides = rideController.getRidesByUser(currentUser.getId());
            boolean hasRatings = false;
            
            for (Ride ride : rides) {
                try {
                    com.rideease.model.Rating rating = ratingService.findRatingByRide(ride);
                    System.out.println("Ride ID: " + ride.getId() + 
                                       " - Driver: " + ride.getDriver().getName() + 
                                       " - Rating: " + rating.getUserRating() + "/5" + 
                                       (rating.getUserComment() != null && !rating.getUserComment().isEmpty() 
                                        ? " - Comment: " + rating.getUserComment() 
                                        : ""));
                    hasRatings = true;
                } catch (Exception e) {
                    // No rating for this ride
                }
            }
            
            if (!hasRatings) {
                System.out.println("No ratings found.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving ratings: " + e.getMessage());
        }
    }

    // Admin Menu
    private void displayAdminMenu() {
        boolean adminMenuRunning = true;
        
        while (adminMenuRunning) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Users");
            System.out.println("2. View All Drivers");
            System.out.println("3. View All Rides");
            System.out.println("4. View All Payments");
            System.out.println("5. View Ride Status Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.println("======================");
            
            int choice = getIntInput("Enter your choice");
            
            try {
                switch (choice) {
                    case 1:
                        viewAllUsers();
                        break;
                    case 2:
                        viewAllDrivers();
                        break;
                    case 3:
                        viewAllRides();
                        break;
                    case 4:
                        viewAllPayments();
                        break;
                    case 5:
                        viewRideStatusStatistics();
                        break;
                    case 0:
                        adminMenuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void viewAllUsers() {
        System.out.println("\n=== All Users ===");
        List<User> users = userController.findAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User user : users) {
                System.out.println("ID: " + user.getId() + 
                                   " - Name: " + user.getName() + 
                                   " - Email: " + user.getEmail() + 
                                   " - Phone: " + user.getPhoneNumber());
            }
        }
    }

    private void viewAllRides() {
        System.out.println("\n=== All Rides ===");
        List<Ride> rides = rideController.getAllRides();
        
        if (rides.isEmpty()) {
            System.out.println("No rides found.");
        } else {
            for (Ride ride : rides) {
                System.out.println("ID: " + ride.getId() + 
                                   " - User: " + ride.getUser().getName() + 
                                   " - Driver: " + (ride.getDriver() != null ? ride.getDriver().getName() : "Not assigned") + 
                                   " - Status: " + ride.getStatus() + 
                                   " - Fare: $" + ride.getFare());
            }
        }
    }

    private void viewAllPayments() {
        System.out.println("\n=== All Payments ===");
        List<com.rideease.model.Payment> payments = paymentController.getAllPayments();
        
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
        } else {
            for (com.rideease.model.Payment payment : payments) {
                System.out.println("ID: " + payment.getId() + 
                                   " - Ride ID: " + payment.getRide().getId() + 
                                   " - User: " + payment.getRide().getUser().getName() + 
                                   " - Amount: $" + payment.getAmount() + 
                                   " - Method: " + payment.getMethod() + 
                                   " - Status: " + (payment.isCompleted() ? "Completed" : "Pending"));
            }
        }
    }

    private void viewRideStatusStatistics() {
        System.out.println("\n=== Ride Status Statistics ===");
        
        int requested = rideController.getRidesByStatus(RideStatus.REQUESTED).size();
        int driverAssigned = rideController.getRidesByStatus(RideStatus.DRIVER_ASSIGNED).size();
        int pickedUp = rideController.getRidesByStatus(RideStatus.PICKED_UP).size();
        int inProgress = rideController.getRidesByStatus(RideStatus.IN_PROGRESS).size();
        int completed = rideController.getRidesByStatus(RideStatus.COMPLETED).size();
        int cancelled = rideController.getRidesByStatus(RideStatus.CANCELLED).size();
        int failed = rideController.getRidesByStatus(RideStatus.FAILED).size();
        
        System.out.println("Requested: " + requested);
        System.out.println("Driver Assigned: " + driverAssigned);
        System.out.println("Picked Up: " + pickedUp);
        System.out.println("In Progress: " + inProgress);
        System.out.println("Completed: " + completed);
        System.out.println("Cancelled: " + cancelled);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (requested + driverAssigned + pickedUp + inProgress + completed + cancelled + failed));
    }

    // Utility methods for input
    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt + ": ");
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return getIntInput(prompt);
        }
    }

    private long getLongInput(String prompt) {
        System.out.print(prompt + ": ");
        try {
            long value = Long.parseLong(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return getLongInput(prompt);
        }
    }

    private double getDoubleInput(String prompt) {
        System.out.print(prompt + ": ");
        try {
            double value = Double.parseDouble(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return getDoubleInput(prompt);
        }
    }

    private boolean getBooleanInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine().toLowerCase();
        if (input.equals("true") || input.equals("yes") || input.equals("y") || input.equals("1")) {
            return true;
        } else if (input.equals("false") || input.equals("no") || input.equals("n") || input.equals("0")) {
            return false;
        } else {
            System.out.println("Please enter true or false.");
            return getBooleanInput(prompt);
        }
    }

    private LocalDateTime getDateTimeInput(String prompt) {
        System.out.print(prompt + " (yyyy-MM-dd HH:mm): ");
        try {
            String input = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Please enter a valid date and time (yyyy-MM-dd HH:mm).");
            return getDateTimeInput(prompt);
        }
    }
}
