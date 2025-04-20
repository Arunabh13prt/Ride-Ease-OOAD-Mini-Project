package com.rideease.controller;

import com.rideease.model.User;
import com.rideease.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for User-related operations
 * Part of MVC architecture: Acts as Controller
 * Team Member: Member 1
 */
@Component
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public User registerUser(String name, String email, String password, String phoneNumber) {
        // Create user object
        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
        
        return userService.registerUser(user);
    }

    public boolean loginUser(String email, String password) {
        return userService.validateLogin(email, password);
    }

    public void logoutUser() {
        userService.logoutCurrentUser();
    }

    public User getCurrentUser() {
        return userService.getCurrentLoggedInUser();
    }

    public User updateUserProfile(Long id, String name, String email, String phoneNumber, String password) {
        // Get existing user
        User user = userService.findUserById(id);
        
        // Update fields
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        
        // Only update password if provided
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        
        return userService.updateUser(user);
    }

    public User getUserById(Long id) {
        return userService.findUserById(id);
    }

    public User getUserByEmail(String email) {
        return userService.findUserByEmail(email);
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return userService.findUserByPhoneNumber(phoneNumber);
    }
    
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }
}
