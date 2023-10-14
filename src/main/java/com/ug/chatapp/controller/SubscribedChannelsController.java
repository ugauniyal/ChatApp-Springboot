package com.ug.chatapp.controller;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ug.chatapp.entity.ChannelModel;
import com.ug.chatapp.entity.SubscribedChannelsModel;
import com.ug.chatapp.entity.UserLoginModel;
import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.ChannelRepository;
import com.ug.chatapp.repos.SubscribedChannelsRepository;
import com.ug.chatapp.repos.UserLoginRepository;
import com.ug.chatapp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class SubscribedChannelsController {

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserLoginRepository userLoginRepository;
    @Autowired
    private SubscribedChannelsRepository subscribedChannelsRepository;


    //    Get all subbed channels of that user
    @GetMapping("/allsubbed")
    public List<String> getAllSubbedChannels(@CookieValue(value = "username") String username) {
        List<SubscribedChannelsModel> subscribedChannelsModels = subscribedChannelsRepository.findByUserModelUsername(username);
        for(int i =0; i < subscribedChannelsModels.size(); i++)
            System.out.println(subscribedChannelsModels.get(i).getChannelModel());
        return subscribedChannelsModels.stream().map(subscribedChannelsModel -> subscribedChannelsModel.getChannelModel().getChannelName()).collect(Collectors.toList());
    }


//    Subscribe the channel
    @PostMapping("/subscribe")
    public ResponseEntity<String> Subscribe(@RequestParam String channel, @CookieValue(value = "username") String username) throws ServerException {

        UserModel userModel = userRepository.findByUsername(username);

        ChannelModel channelModel = channelRepository.findByChannelName(channel);


        if (userModel != null && channelModel != null) {
            SubscribedChannelsModel subscribedChannelsModel = new SubscribedChannelsModel();
            subscribedChannelsModel.setUserModel(userModel);
            subscribedChannelsModel.setChannelModel(channelModel);

            subscribedChannelsRepository.save(subscribedChannelsModel);

            return new ResponseEntity<>("Subscribed to " + channel, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User or channel not found", HttpStatus.NOT_FOUND);
        }

    }



//    Unsubscribe the channel
    @DeleteMapping("/unsubscribe")
    public String Unsubscribe(@RequestParam String channel, @CookieValue(value = "username") String username) {


        UserModel userModel = userRepository.findByUsername(username);
        ChannelModel channelModel = channelRepository.findByChannelName(channel);


        if (userModel == null || channelModel == null) {
            return "Unauthorized Call";
        }

        SubscribedChannelsModel subscribedChannelsModel =
                subscribedChannelsRepository.findByUserModelIdAndChannelModelId(userModel.getId(), channelModel.getId());



        subscribedChannelsRepository.delete(subscribedChannelsModel);

        return "Deleted";
    }

}
