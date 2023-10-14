package com.ug.chatapp.controller;

import com.ug.chatapp.dto.TopChannelDto;
import com.ug.chatapp.entity.ChannelModel;
import com.ug.chatapp.entity.UserModel;
import com.ug.chatapp.repos.ChannelRepository;
import com.ug.chatapp.repos.UserLoginRepository;
import com.ug.chatapp.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChannelController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;


    @Autowired
    private com.ug.chatapp.services.ChannelService channelService;



//    Get all channels.
//    @Cacheable(value = "channel")
    @GetMapping("/getChannels")
    public List<String> getAllChannels() {
        List<ChannelModel> channelModelList = channelRepository.findAll();
        for(int i =0; i < channelModelList.size(); i++)
            System.out.println(channelModelList.get(i).getChannelName());
        return channelModelList.stream().map(channelModel -> channelModel.getChannelName()).collect(Collectors.toList());
    }


//    Delete all channels.
    @DeleteMapping("/deleteChannel")
    public String deleteChannelByName(@RequestParam String name, @CookieValue(value = "username") String username) {

        UserModel userModel = userRepository.findByUsername(username);

        if(userModel == null) {
            return "You're not logged in or the user does not exist.";
        }

        String email = userModel.getEmail();

        ChannelModel channelModel = channelRepository.findByChannelNameAndUserModelEmail(name, email);

        if (channelModel == null) {
            return "No such channel";
        }

        Long id = channelModel.getId();

        userModel = channelModel.getUserModel();

        if (userModel == null) {
            return "No such user";
        }

        if (!userModel.getEmail().equals(email)) {
            return "Unauthorized Call";
        }

        channelRepository.deleteById(id);
        return "Deleted";
    }


//    Create a new channel.
    @PostMapping(value = "/createChannel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChannelModel> createChannel(@RequestParam String newChannel, @CookieValue(value = "username") String username) throws ServerException {

        UserModel userModel = userRepository.findByUsername(username);

        if(userModel == null) {
            throw new ServerException("You're not logged in or the user does not exist.");
        }


        ChannelModel channelModel = new ChannelModel();

        ChannelModel checkChannel = channelRepository.findByChannelName(newChannel);

        if(checkChannel != null) {
            throw new ServerException("You've already made this channel");
        }

        System.out.println(userModel.getEmail());
        channelModel.setChannelName(newChannel);
        channelModel.setUserModel(userModel);


        channelRepository.save(channelModel);

        if (channelModel == null || channelModel.getChannelName() == "" || channelModel.getUserModel() == null) {
            throw new ServerException("Fields are not filled");
        }


        return new ResponseEntity<>(channelModel, HttpStatus.CREATED);

    }

//    Update a channel.
    @PostMapping(value = "/updateChannel")
    public String updateUser(@RequestParam String channelName, @RequestParam String newChannelName, @CookieValue(value = "username") String username){

        UserModel userModel = userRepository.findByUsername(username);

        if(userModel == null) {
            return "You're not logged in or there is no such user.";
        }

        ChannelModel channelModel = channelRepository.findByChannelName(channelName);
        Long id = channelModel.getId();


        channelModel.setChannelName(newChannelName);
        channelRepository.save(channelModel);


        return "Channel Updated";

    }


//    Get Top 10 Channels Of The Week.
    @GetMapping("/top")
    public List<TopChannelDto> getTopChannels() {
        return channelService.getTopChannels();
    }



}
