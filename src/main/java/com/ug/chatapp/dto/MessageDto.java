package com.ug.chatapp.dto;

import com.ug.chatapp.entity.ChannelModel;
import com.ug.chatapp.entity.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageDto {
    private long id;
    private String messageText;
    private Date createdAt;
    private UserModel userModel;
    private ChannelModel channelModel;
    // Other fields as needed, or references to other DTOs

    // Constructors, getters, and setters

}
