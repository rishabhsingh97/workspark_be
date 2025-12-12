package com.workspark.commonconfig.service;

import com.workspark.commonconfig.models.entity.RedisAuthUser;

import java.util.Optional;

public interface RedisAuthUserService {
    void saveUser(RedisAuthUser redisAuthUser);
    Optional<RedisAuthUser> getUser(String redisAuthUserId);
    boolean existsUser(String redisAuthUserId);
    void deleteUser(String redisAuthUserId);
}
