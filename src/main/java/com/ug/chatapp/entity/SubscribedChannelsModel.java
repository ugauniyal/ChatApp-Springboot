package com.ug.chatapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subscribed_model")
public class SubscribedChannelsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel userModel;

    @ManyToOne(cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private ChannelModel channelModel;


}
