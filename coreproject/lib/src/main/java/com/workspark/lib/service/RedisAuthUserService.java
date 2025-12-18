package com.workspark.lib.service;

import com.workspark.lib.models.entity.RedisAuthUser;

import java.util.Optional;

public interface RedisAuthUserService {
    void saveUser(RedisAuthUser redisAuthUser);
    Optional<RedisAuthUser> getUser(String redisAuthUserId);
    boolean existsUser(String redisAuthUserId);
    void deleteUser(String redisAuthUserId);
}
