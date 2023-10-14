package com.ug.chatapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "message_model")
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    private String message_text;

    @Temporal(TemporalType.TIMESTAMP) // Specify temporal type for the created_at field
    @Column(name = "created_at")
    private Date created; // Add the created_at field

    @ManyToOne(cascade=CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel userModel;

    @ManyToOne(cascade=CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private ChannelModel channelModel;

}
