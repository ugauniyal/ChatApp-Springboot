package com.ug.chatapp;

import com.ug.chatapp.dto.TopChannelDto;
import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.UserRepository;
import com.ug.chatapp.services.ChannelService;
import jakarta.mail.internet.MimeMessage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;

@Component
public class EmailJob implements Job {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        sendEmailToAllUsers();
    }

    private void sendEmailToAllUsers() {
        List<UserModel> users = userRepository.findAll(); // Fetch all users from the database
        for (UserModel user : users) {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(user.getEmail()); // Send email to each user
                helper.setSubject("Top 10 Channels");

                String emailContent = generateTopChannelsEmailContent();

                helper.setText(emailContent, true); // Use true to enable HTML in the email content

                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String generateTopChannelsEmailContent() {
        List<TopChannelDto> topChannels = channelService.getTopChannels();

        StringBuilder content = new StringBuilder("Top 10 Channels:<br>");

        for (int i = 0; i < topChannels.size(); i++) {
            TopChannelDto channel = topChannels.get(i);
            content.append(i + 1).append(". ")
                    .append("Channel Name: ").append(channel.getChannelName()).append(", ")
                    .append("Message Count: ").append(channel.getMessageCount())
                    .append("<br>");
        }

        return content.toString();
    }
}
