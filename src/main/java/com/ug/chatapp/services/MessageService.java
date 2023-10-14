package com.ug.chatapp.services;

import com.ug.chatapp.entity.MessageModel;
import com.ug.chatapp.repos.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public void saveMessage(MessageModel message) {
        messageRepository.save(message);
    }
}
