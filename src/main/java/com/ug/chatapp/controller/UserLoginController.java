package com.ug.chatapp.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ug.chatapp.entity.UserLoginModel;
import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.UserLoginRepository;
import com.ug.chatapp.repos.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;


@RestController
@EnableCaching
public class UserLoginController {
    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    public UserLoginController(UserRepository userRepository,
                               UserLoginRepository userLoginRepository) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

//    Login Endpoint
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginModel> login(@RequestParam String email, @RequestParam String password, HttpServletResponse response) {

        UserModel userModel = userRepository.findByEmail(email);

        if (userModel.isEnabled() == true) {

            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), userModel.getPassword());
            if (result.verified == true) {
                UserLoginModel newUserLoginMode = new UserLoginModel();


                String token = getSaltString();
                newUserLoginMode.setToken(token);

                newUserLoginMode.setUserModel(userModel);


//                Adding cookie to get the current logged-in user for further functionality.
                Cookie cookie = new Cookie("username", userModel.getUsername());
                // add cookie in server response
                cookie.setMaxAge(86400); // A day is equal to 86400 seconds.
                response.addCookie(cookie);



                UserLoginModel userLoginModel = userLoginRepository.save(newUserLoginMode);
                return new ResponseEntity<>(userLoginModel, HttpStatus.CREATED);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


//    Logout Endpoint
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public String logout(@CookieValue(value = "username") String username, HttpServletResponse response) {
        UserModel userModel = userRepository.findByUsername(username);

        UserLoginModel userLoginModel = userLoginRepository.findByUserModelId(userModel.getId());

        if (userLoginModel == null) {
            return "No user Logged In";
        }


        userLoginRepository.delete(userLoginModel);

//        Deleting existing username cookie so that when user logs out, cookie gets null for another logged-in user.
        Cookie cookie = new Cookie("username", null);
        cookie.setMaxAge(0); // delete cookie
        response.addCookie(cookie);

        return "Logged out";
    }


//    Just to check if cookie is being stored.
    @GetMapping("/get")
    public String getCookie(@CookieValue(value = "username",
            defaultValue = "No username found") String username) {

        return "Username is: " + username;
    }

//    @GetMapping("/set")
//    public String setCookie(@CookieValue(value = "username",
//            defaultValue = "No username found") String username, HttpServletResponse response) {
//
//        Cookie cookie = new Cookie("username", "Utkarsh");
//        // add cookie in server response
//        cookie.setMaxAge(86400); // A day is equal to 86400 seconds.
//        response.addCookie(cookie);
//
//        return "Done";
//    }
}
