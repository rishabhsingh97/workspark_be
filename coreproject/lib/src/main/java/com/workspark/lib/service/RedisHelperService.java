package com.workspark.lib.service;

public interface RedisHelperService {
    void saveToRedis(String key, Object value);
    Object getFromRedis(String key);
    void deleteFromRedis(String key);
    public boolean existsInRedis(String key);
}
