package com.workspark.authenticationservice.exceptions.customExceptions;

public class RedisConnectionException extends RuntimeException {
    public RedisConnectionException(String message) {
        super(message);
    }
}