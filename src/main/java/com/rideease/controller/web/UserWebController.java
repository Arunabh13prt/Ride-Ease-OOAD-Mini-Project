package com.rideease.controller.web;

import com.rideease.controller.UserController;
import com.rideease.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Web controller for handling user-related operations in browser
 */
@Controller
@RequestMapping("/web/users")
public class UserWebController {

    private final UserController userController;

    @Autowired
    public UserWebController(UserController userController) {
        this.userController = userController;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register-user";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes) {

        try {
            User user = userController.registerUser(name, email, password, phoneNumber);
            redirectAttributes.addFlashAttribute("successMessage", "User registered successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to register user: " + e.getMessage());
            return "redirect:/web/users/register";
        }
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userController.getUserById(id);
        model.addAttribute("user", user);
        return "user-details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userController.getUserById(id);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "password", required = false) String password,
            RedirectAttributes redirectAttributes) {

        try {
            User user = userController.updateUserProfile(id, name, email, phoneNumber, password);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user: " + e.getMessage());
            return "redirect:/web/users/" + id + "/edit";
        }
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes) {

        try {
            boolean isAuthenticated = userController.loginUser(email, password);
            
            if (isAuthenticated) {
                User currentUser = userController.getUserByEmail(email);
                redirectAttributes.addFlashAttribute("successMessage", "Welcome, " + currentUser.getName() + "!");
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
                return "redirect:/web/users/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Login failed: " + e.getMessage());
            return "redirect:/web/users/login";
        }
    }

    @PostMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        userController.logoutUser();
        redirectAttributes.addFlashAttribute("successMessage", "Logged out successfully");
        return "redirect:/";
    }
}