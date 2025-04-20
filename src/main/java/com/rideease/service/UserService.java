package com.rideease.service;

import com.rideease.model.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User findUserById(Long id);
    User findUserByEmail(String email);
    User findUserByPhoneNumber(String phoneNumber);
    List<User> findAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
    boolean validateLogin(String email, String password);
    User getCurrentLoggedInUser();
    void setCurrentLoggedInUser(User user);
    void logoutCurrentUser();
}
