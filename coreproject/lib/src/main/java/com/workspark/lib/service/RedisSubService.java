package com.workspark.lib.service;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public interface RedisSubService extends MessageListener {
}
