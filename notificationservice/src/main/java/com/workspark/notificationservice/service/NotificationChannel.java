package com.workspark.notificationservice.service;

import com.workspark.models.pojo.Notification;
import org.springframework.stereotype.Service;

@Service
public interface NotificationChannel {

    boolean send(Notification notificationDTO);

}
