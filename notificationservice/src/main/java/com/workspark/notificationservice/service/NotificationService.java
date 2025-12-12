package com.workspark.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.commonconfig.service.impl.RedisSubServiceImpl;
import com.workspark.models.enums.NotificationMode;
import com.workspark.models.pojo.Notification;
import com.workspark.notificationservice.exceptions.customExceptions.NotificationException;
import com.workspark.notificationservice.factory.NotificationChannelFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for handling notifications received from Redis.
 * It implements the RedisSubService interface and is annotated with @Service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService extends RedisSubServiceImpl {

    private final NotificationChannelFactory notificationServiceFactory;

    /**
     * This method is called when a message is received from the Redis channel.
     * It parses the message, determines the notification modes, and sends the notification to the appropriate channels.
     *
     * @param message The message received from the Redis channel
     */
    @Override
    public void handleMessage(Message message) {
        log.info("{}", message);
        String jsonMessage = new String(message.getBody());
        log.info("Received message: {}", jsonMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        Notification notificationDto = null;
        try {
            notificationDto = objectMapper.readValue(jsonMessage, Notification.class);
        } catch (JsonProcessingException e) {
            throw new NotificationException("Error parsing JSON message: " + e.getMessage());
        }
        Set<NotificationMode> modes = this.getNotificationModes(notificationDto);

        Notification finalNotificationDto = notificationDto;
        modes.forEach(mode -> {
            NotificationChannel service = notificationServiceFactory.getChannel(mode);
            if (service != null) {
                boolean success = service.send(finalNotificationDto);
                if (success) {
                    log.info("{} notification sent successfully", mode);
                } else {
                    log.error("Failed to send {} notification", mode);
                }
            } else {
                log.error("No channel found for mode: {}", mode);
            }
        });
    }

    /**
     * This method determines the notification modes based on the notification DTO.
     *
     * @param notificationDto The notification DTO
     * @return Set of NotificationMode
     */
    private Set<NotificationMode> getNotificationModes(Notification notificationDto) {
        Set<NotificationMode> modes = new HashSet<>();

        if(notificationDto.getEmail() != null) {
            modes.add(NotificationMode.EMAIL);
        }

        return modes;
    }
}
