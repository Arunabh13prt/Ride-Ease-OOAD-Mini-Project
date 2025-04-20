package com.rideease.service.impl;

import com.rideease.exception.UserNotFoundException;
import com.rideease.model.User;
import com.rideease.repository.UserRepository;
import com.rideease.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of UserService interface
 * Follows Singleton Pattern (Spring managed singleton)
 * Follows Single Responsibility Principle (SRP) from SOLID
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private User currentLoggedInUser;

    @Override
    public User registerUser(User user) {
        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new IllegalArgumentException("User with this phone number already exists");
        }
        
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found with phone number: " + phoneNumber));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        // Check if user exists
        User existingUser = findUserById(user.getId());
        
        // Update user details
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        
        // Only update password if it's not empty
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        // Check if user exists
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Override
    public boolean validateLogin(String email, String password) {
        try {
            User user = findUserByEmail(email);
            if (user.getPassword().equals(password)) {
                currentLoggedInUser = user;
                return true;
            }
            return false;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    @Override
    public User getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    @Override
    public void setCurrentLoggedInUser(User user) {
        this.currentLoggedInUser = user;
    }

    @Override
    public void logoutCurrentUser() {
        this.currentLoggedInUser = null;
    }
    
    @Override
    public void updateUserRating(Long userId) {
        // Get the user
        User user = findUserById(userId);
        
        // In a real implementation, we would calculate the average rating from the database
        // and update the user's rating field
        // For now, this is just a placeholder implementation
        
        // The actual calculation is done in the RatingRepository with getAverageUserRating
        // and used directly in the RatingController
    }
}
