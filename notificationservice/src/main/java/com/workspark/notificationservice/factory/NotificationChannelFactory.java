package com.workspark.notificationservice.factory;

import com.workspark.models.enums.NotificationMode;
import com.workspark.notificationservice.service.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class is responsible for creating and managing notification channels.
 * It provides a factory method to retrieve a notification channel based on the notification mode.
 */
@Component
@RequiredArgsConstructor
public class NotificationChannelFactory {

    private final Map<NotificationMode, NotificationChannel> channels;

    public NotificationChannel getChannel(NotificationMode mode) {
        return channels.get(mode);
    }

}

