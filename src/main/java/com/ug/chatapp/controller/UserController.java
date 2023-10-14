package com.ug.chatapp.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ug.chatapp.entity.UserLoginModel;
import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.UserLoginRepository;
import com.ug.chatapp.repos.UserRepository;
import com.ug.chatapp.services.UserServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserServices userServices;
    @Autowired
    private UserLoginRepository userLoginRepository;


    //    Get all the users
    @GetMapping("/")
    public List<String> getAllUsers() {
        List<UserModel> userModelList = userRepository.findAll();
        for(int i =0; i < userModelList.size(); i++)
            System.out.println(userModelList.get(i).getUsername());
        return userModelList.stream().map(userModel -> userModel.getUsername()).collect(Collectors.toList());
    }

//    Get particular user

//  Get user by id
    @GetMapping("/getUserById")
    public String getExistingUserById(@RequestParam Long id) {
        UserModel user = userRepository.findById(id).get();
        System.out.println(user);
        System.out.println(user.getUsername());
        return user.getUsername();
    }


    //    Delete the user.
    @PostMapping("/deleteUser")
    public String deleteExistingUserByEmail(@CookieValue(value = "username") String username, HttpServletResponse response) {
        UserModel userModel = userRepository.findByUsername(username);
        Long id = userModel.getId();
        UserLoginModel userLoginModel = userLoginRepository.findByUserModelId(id);

        if (userModel.isEnabled() && userLoginModel.getToken() != null) {
            userLoginRepository.deleteById(userLoginModel.getId());
            userRepository.deleteById(id);

            Cookie cookie = new Cookie("username", null);
            cookie.setMaxAge(0); // delete cookie
            response.addCookie(cookie);

            return "Deleted";
        }
        return "Failed deleting the profile. Check if profile is available or enabled.";
    }


//    Update user endpoint.
    @PostMapping("/updateUser")
    public String updateUserUsingId(
            @CookieValue(value = "username") String username,
            @RequestParam String newUsername,
            @RequestParam String newEmail,
            @RequestParam String newPassword,
            HttpServletResponse response) {

        UserModel userModel = userRepository.findByUsername(username);
        UserLoginModel userLoginModel = userLoginRepository.findByUserModelId(userModel.getId());

        if (userModel.isEnabled() && userLoginModel.getToken() != null) {
            userModel.setUsername(newUsername);
            userModel.setEmail(newEmail);

            // Check if a new password is provided and update it
            if (!newPassword.isEmpty()) {
                // Encrypt the new password using BCrypt
                String bcryptHashString = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
                userModel.setPassword(bcryptHashString);
            }

            userRepository.save(userModel);

            Cookie cookie = new Cookie("username", newUsername);
            // add cookie in server response
            cookie.setMaxAge(86400); // A day is equal to 86400 seconds.
            response.addCookie(cookie);

            return "Updated";
        }

        return "Failed updating the profile. Check if the profile is available or enabled.";
    }


//  Create User Endpoint.
    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel newUser) throws ServerException {

        String randomCode = RandomString.make(64);


        String encryption = newUser.getPassword();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, encryption.toCharArray());


        newUser.setVerificationCode(randomCode);
        newUser.setPassword(bcryptHashString);

        UserModel user = userRepository.save(newUser);

        if (user == null || user.getUsername() == "" || user.getEmail() == "" || user.getPassword() == "") {
            throw new ServerException("Fields are not filled");
        }

        else {
            String content = "Dear "
                    + "Please click the link below to verify your registration:"
                    + "[[URL]]"
                    + " Thank you"
                    + "UGINES.";


            content = content.replace("[[name]]", user.getUsername());


            String verifyURL =  "http://localhost:8080/verify?code=" + user.getVerificationCode();

            content = content.replace("[[URL]]", verifyURL);

            userServices.sendVerificationEmail(newUser.getEmail(), content);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }


//    Verify email endpoint.
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userServices.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

}
