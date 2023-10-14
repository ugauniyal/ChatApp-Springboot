package com.ug.chatapp;

import com.ug.chatapp.dto.TopChannelDto;
import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.UserRepository;
import com.ug.chatapp.services.ChannelService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SendTopChannelsEmailJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(SendTopChannelsEmailJob.class);

//    @Autowired
    private ChannelService topChannelsService;

//    @Autowired
    private JavaMailSender mailSender;

//    @Autowired
    private UserRepository userRepository;


    public SendTopChannelsEmailJob() {
        this.topChannelsService = null; // Initialize to null or provide a default value if needed
        this.mailSender = null; // Initialize to null or provide a default value if needed
        this.userRepository = null; // Initialize to null or provide a default value if needed
    }

    public SendTopChannelsEmailJob(ChannelService topChannelsService, JavaMailSender mailSender, UserRepository userRepository) {
        this.topChannelsService = topChannelsService;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("SendTopChannelsEmailJob started");
        try {
            List<TopChannelDto> topChannels = topChannelsService.getTopChannels();
            logger.debug("Top channels retrieved successfully");

            String emailContent = generateEmailContent(topChannels);
            logger.debug("Email content generated");

            List<UserModel> users = userRepository.findAll();
            logger.debug("Found {} users", users.size());

            for (UserModel user : users) {
                logger.info("Sending email to user: {}", user.getEmail());
                sendEmail(user.getEmail(), "Top 10 Channels of the Week", emailContent);
                logger.debug("Email sent to user: {}", user.getEmail());
            }

            logger.info("SendTopChannelsEmailJob completed successfully");
        } catch (Exception e) {
            logger.error("An error occurred in SendTopChannelsEmailJob", e);
            throw new JobExecutionException(e);
        }
    }

    private String generateEmailContent(List<TopChannelDto> topChannels) {
        StringBuilder content = new StringBuilder();

        // Add a heading or introduction to the email content
        content.append("Top 10 Channels of the Week:\n\n");

        // Iterate through the top channels and add them to the content
        for (int i = 0; i < topChannels.size(); i++) {
            TopChannelDto channel = topChannels.get(i);
            content.append(i + 1).append(". "); // Channel number
            content.append("Channel Name: ").append(channel.getChannelName()); // Channel name
            content.append("\n");
            content.append("Message Count: ").append(channel.getMessageCount()); // Message count
            content.append("\n\n");
        }

        // You can customize the formatting and structure of the email content as needed

        return content.toString();
    }



    private void sendEmail(String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gauniyalutkarsh16@gmail.com"); // Set a sender email address
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
}
