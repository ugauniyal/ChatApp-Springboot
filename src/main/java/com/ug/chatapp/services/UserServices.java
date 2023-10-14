package com.ug.chatapp.services;

import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JavaMailSender mailSender;


//    public void register(UserModel user, String siteURL) {
//
//    }

    public void sendVerificationEmail(String toEmail, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("gauniyalutkarsh16@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject("Please verify your registration");


        mailSender.send(message);

        System.out.println("Mail sent successfully!");
    }


    public boolean verify(String verificationCode) {
        UserModel user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }
}
