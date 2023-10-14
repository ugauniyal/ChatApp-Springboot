package com.ug.chatapp.repos;


import com.ug.chatapp.entity.SubscribedChannelsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscribedChannelsRepository extends JpaRepository<SubscribedChannelsModel,Long> {

    @Query(value = "SELECT * FROM subscribed_model u WHERE u.user_id = :id1 AND u.channel_id = :id2", nativeQuery = true)
    public SubscribedChannelsModel findByUserModelIdAndChannelModelId(@Param("id1") Long id1, @Param("id2") Long id2);

    public List<SubscribedChannelsModel> findByUserModelUsername(String username);

}
