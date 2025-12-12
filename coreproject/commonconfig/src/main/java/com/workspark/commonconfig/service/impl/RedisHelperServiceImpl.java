package com.workspark.commonconfig.service.impl;

import com.workspark.commonconfig.service.RedisHelperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "workspark.redis", havingValue = "true", matchIfMissing = true)
public class RedisHelperServiceImpl implements RedisHelperService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Save data to Redis.
     *
     * @param key   The Redis key
     * @param value The value to be saved
     */
    public void saveToRedis(String key, Object value) {
        log.info("Saving data to Redis for key: {}", key);
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("Data successfully saved to Redis. Key: {}, Value: {}", key, value);
        } catch (Exception e) {
            log.error("Error saving data to Redis. Key: {}", key, e);
        }
    }

    /**
     * Retrieve data from Redis.
     *
     * @param key The Redis key
     * @return The value associated with the key, or null if not found
     */
    public Object getFromRedis(String key) {
        log.info("Retrieving data from Redis for key: {}", key);
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (Objects.isNull(value)) {
                log.warn("No data found in Redis for key: {}", key);
            } else {
                log.debug("Data retrieved from Redis. Key: {}, Value: {}", key, value);
            }
            return value;
        } catch (Exception e) {
            log.error("Error retrieving data from Redis. Key: {}", key, e);
            return null;
        }
    }

    /**
     * Delete data from Redis.
     *
     * @param key The Redis key
     */
    public void deleteFromRedis(String key) {
        log.info("Deleting data from Redis for key: {}", key);
        try {
            boolean deleted = Boolean.TRUE.equals(redisTemplate.delete(key));
            if (deleted) {
                log.debug("Data successfully deleted from Redis. Key: {}", key);
            } else {
                log.warn("No data found to delete in Redis for key: {}", key);
            }
        } catch (Exception e) {
            log.error("Error deleting data from Redis. Key: {}", key, e);
        }
    }

    /**
     * Check if a key exists in Redis.
     *
     * @param key The Redis key
     * @return True if the key exists, false otherwise
     */
    public boolean existsInRedis(String key) {
        log.info("Checking existence of key in Redis: {}", key);
        try {
            boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(key));
            log.debug("Existence check result for key {}: {}", key, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking existence of key in Redis. Key: {}", key, e);
            return false;
        }
    }
}
