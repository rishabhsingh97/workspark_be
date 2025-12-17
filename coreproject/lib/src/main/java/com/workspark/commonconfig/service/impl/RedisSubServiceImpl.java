package com.workspark.commonconfig.service.impl;

import com.workspark.commonconfig.service.RedisSubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public abstract class RedisSubServiceImpl implements RedisSubService {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        this.handleMessage(message);
    }

    public abstract void handleMessage(Message message);
}
