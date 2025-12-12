package com.workspark.notificationservice.config;

import com.workspark.models.enums.NotificationMode;
import com.workspark.notificationservice.service.NotificationChannel;
import com.workspark.notificationservice.service.impl.EmailNotificationChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * This configuration class is responsible for setting up the notification channels.
 * It creates a map of notification channels, where the key is the notification mode,
 * and the value is the corresponding notification channel implementation.
 */
@Configuration
public class NotificationChannelConfig {

    @Bean
    public Map<NotificationMode, NotificationChannel> notificationChannels(
            EmailNotificationChannel emailNotificationService
    ) {
        return Map.of(
                NotificationMode.EMAIL, emailNotificationService
        );
    }
}

