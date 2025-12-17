package com.workspark.commonconfig.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

/**
 * Configuration class for setting up Redis connection and RedisTemplate bean.
 * This configuration is used for setting up the Redis connection and RedisTemplate bean
 * for the application to interact with Redis.
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "workspark.redis", havingValue = "true", matchIfMissing = true)
public class RedisConfig {

    /**
     * Creates and returns a RedisTemplate bean.
     * This bean is used to interact with Redis.
     *
     * @param connectionFactory - RedisConnectionFactory bean
     * @return - RedisTemplate bean
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return redisTemplate;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("message");
    }

}
