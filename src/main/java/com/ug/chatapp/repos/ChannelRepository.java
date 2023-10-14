package com.ug.chatapp.repos;

import com.ug.chatapp.entity.ChannelModel;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelModel,Long> {


    public ChannelModel findByChannelName(String name);

    public ChannelModel findByUserModelId(Long id);

    public ChannelModel findByChannelNameAndUserModelEmail(String name, String email);
}
