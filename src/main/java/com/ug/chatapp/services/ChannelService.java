    package com.ug.chatapp.services;

    import com.ug.chatapp.dto.TopChannelDto;
    import com.ug.chatapp.entity.ChannelModel;
    import com.ug.chatapp.repos.MessageRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.stream.Collectors;


    @Service
    public class ChannelService {

        @Autowired
        private MessageRepository messageRepository;

        public List<TopChannelDto> getTopChannels() {
            List<Object[]> topChannelsWithMessageCount = messageRepository.countMessagesPerChannel();
            return topChannelsWithMessageCount.stream()
                    .map(result -> {
                        ChannelModel channelModel = (ChannelModel) result[0];
                        Long messageCount = (Long) result[1];
                        return new TopChannelDto(channelModel.getChannelName(), messageCount);
                    })
                    .sorted((c1, c2) -> Long.compare(c2.getMessageCount(), c1.getMessageCount()))
                    .limit(10)
                    .collect(Collectors.toList());
        }
    }
