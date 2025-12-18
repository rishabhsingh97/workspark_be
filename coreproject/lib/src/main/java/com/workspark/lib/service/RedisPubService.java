package com.workspark.lib.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.models.pojo.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPubService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final ChannelTopic topic;

    public void publish(Notification notificationDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(notificationDto);
        redisTemplate.convertAndSend(topic.getTopic(), jsonMessage);
    }

}
