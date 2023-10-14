package com.ug.chatapp.controller;

import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserInfoController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/currentUser")
    public String getCurrentUserInfo(@CookieValue(value = "username") String username, Model model) {
        // Check if the username is empty or null (not logged in)
        if (username == null || username.isEmpty()) {
            return "redirect:/login"; // Redirect to the login page or appropriate page
        }

        // Fetch the user information based on the username
        UserModel userModel = userRepository.findByUsername(username);

        if (userModel != null) {
            model.addAttribute("user", userModel);
            return "user-info"; // This should match the name of your HTML file without the extension
        } else {
            // Handle the case where the user is not found
            return "error"; // Create an "error.html" page for displaying error messages
        }
    }


    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        // You can add any necessary model attributes here
        return "sign-up"; // This should match the name of your HTML file without the extension
    }

}
