package com.workspark.commonconfig.service.impl;

import com.workspark.commonconfig.models.entity.RedisAuthUser;
import com.workspark.commonconfig.repository.RedisAuthUserRepository;
import com.workspark.commonconfig.service.RedisAuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "workspark.redis", havingValue = "true", matchIfMissing = true)
public class RedisAuthUserServiceImpl implements RedisAuthUserService {

    private final RedisAuthUserRepository redisUserRepository;

    /**
     * Save the user data to Redis.
     *
     * @param redisAuthUser The user data to be saved.
     */
    public void saveUser(RedisAuthUser redisAuthUser) {
        Assert.notNull(redisAuthUser, "User data cannot be null");
        log.info("Saving user data for userId: {}", redisAuthUser.getRedisUserId());
        try {
            redisUserRepository.save(redisAuthUser);
            log.debug("Detailed save operation completed for userId: {}", redisAuthUser.getRedisUserId());
        } catch (Exception e) {
            log.error("Failed to save user data for userId: {}", redisAuthUser.getRedisUserId(), e);
        }
    }

    /**
     * Retrieve the user data from Redis.
     *
     * @param redisAuthUserId The unique identifier for the user.
     * @return The user data retrieved from Redis, or null if the user doesn't exist.
     */
    public Optional<RedisAuthUser> getUser(String redisAuthUserId) {
        log.info("Fetching user data for userId: {}", redisAuthUserId);
        try {
            Optional<RedisAuthUser> redisAuthUser = redisUserRepository.findById(redisAuthUserId);
            if (redisAuthUser.isPresent()) {
                log.info("User data found for userId: {}", redisAuthUserId);
                log.debug("Detailed user data: {}", redisAuthUser.get());
            } else {
                log.warn("No user data found in Redis for userId: {}", redisAuthUserId);
            }
            return redisAuthUser;
        } catch (Exception e) {
            log.error("Error retrieving user data for userId: {}", redisAuthUserId, e);
            return Optional.empty();
        }
    }

    /**
     * Check if the user exists in Redis.
     *
     * @param redisAuthUserId The unique identifier for the user.
     * @return True if the user exists in Redis, false otherwise.
     */
    public boolean existsUser(String redisAuthUserId) {
        log.info("Checking if user exists for userId: {}", redisAuthUserId);
        try {
            boolean exists = redisUserRepository.existsById(redisAuthUserId);
            log.debug("Existence check result for userId {}: {}", redisAuthUserId, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking existence for userId: {}", redisAuthUserId, e);
            return false;
        }
    }

    /**
     * Delete user data from Redis.
     *
     * @param redisAuthUserId The unique identifier for the user.
     */
    public void deleteUser(String redisAuthUserId) {
        log.info("Deleting user data for userId: {}", redisAuthUserId);
        try {
            redisUserRepository.deleteById(redisAuthUserId);
            log.debug("User data deleted successfully for userId: {}", redisAuthUserId);
        } catch (Exception e) {
            log.error("Error deleting user data for userId: {}", redisAuthUserId, e);
        }
    }
}
