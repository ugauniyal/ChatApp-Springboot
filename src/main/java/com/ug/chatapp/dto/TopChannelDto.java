package com.ug.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopChannelDto {
    private String channelName;
    private Long messageCount;
}
