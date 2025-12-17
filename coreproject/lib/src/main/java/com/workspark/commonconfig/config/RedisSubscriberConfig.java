package com.workspark.commonconfig.config;

import com.workspark.commonconfig.service.RedisSubService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Configuration class for setting up Redis connection and RedisTemplate bean.
 * This configuration is used for setting up the Redis connection and RedisTemplate bean
 * for the application to interact with Redis.
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "workspark.redis.messaging.sub", havingValue = "true", matchIfMissing = false)
public class RedisSubscriberConfig {

    private final RedisSubService redisSubService;
    private final ChannelTopic topic;

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(redisSubService);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter(), topic);
        return container;
    }
}
