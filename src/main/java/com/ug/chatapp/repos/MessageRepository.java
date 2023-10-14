package com.ug.chatapp.repos;

import com.ug.chatapp.dto.MessageDto;
import com.ug.chatapp.entity.ChannelModel;
import com.ug.chatapp.entity.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {

    List<MessageModel> findByChannelModel(ChannelModel channelModel);
    @Query("SELECT NEW com.ug.chatapp.dto.MessageDto(m.id, m.message_text, m.created, m.userModel, m.channelModel) FROM MessageModel m WHERE m.channelModel.channelName = :channelName ORDER BY m.created ASC")
    List<MessageDto> findByChannelModelChannelNameOrderByCreatedAsc(@Param("channelName") String channelName);


    @Query("SELECT m.channelModel, COUNT(m) FROM MessageModel m GROUP BY m.channelModel")
    List<Object[]> countMessagesPerChannel();


}

