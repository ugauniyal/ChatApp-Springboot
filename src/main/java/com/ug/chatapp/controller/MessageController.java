package com.ug.chatapp.controller;

import com.ug.chatapp.dto.MessageDto;
import com.ug.chatapp.entity.ChannelModel;
import com.ug.chatapp.entity.MessageModel;
import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.ChannelRepository;
import com.ug.chatapp.repos.MessageRepository;
import com.ug.chatapp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @MessageMapping("/chat/{channelName}.sendMessage")
    @SendTo("/topic/{channelName}")
    public MessageModel sendMessage(@CookieValue(value = "username") String username, @DestinationVariable String channelName, @Payload MessageModel message) {

        username = "UGINES";
        System.out.println(username);

        System.out.println(channelName);

        UserModel userModel = userRepository.findByUsername(username);
        ChannelModel channelModel = channelRepository.findByChannelName(channelName);

        if (userModel == null) {
            System.out.println("Can't find any user by this username.");
            System.out.println(username);
            return null;
        }


        if (channelModel == null) {
            System.out.println("Can't find any channel by this channel name.");
            return null;
        }

        // Create a new message
        MessageModel messageModel = new MessageModel();
        messageModel.setMessage_text(message.getMessage_text());
        messageModel.setUserModel(userModel);
        messageModel.setChannelModel(channelModel);
        messageModel.setCreated(new Date());


        // Save the message
        try {
            messageRepository.save(messageModel);
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception for debugging
        }

        System.out.println("UserModel username: " + messageModel.getUserModel().getUsername());

        return message;
    }

    @GetMapping("/messages/{channelName}")
    public ResponseEntity<List<MessageDto>> getOldMessages(@PathVariable String channelName) {
        List<MessageDto> oldMessages = messageRepository.findByChannelModelChannelNameOrderByCreatedAsc(channelName);
        return ResponseEntity.ok(oldMessages);
    }

}