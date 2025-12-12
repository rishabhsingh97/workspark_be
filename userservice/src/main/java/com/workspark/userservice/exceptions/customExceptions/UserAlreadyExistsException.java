package com.workspark.userservice.exceptions.customExceptions;

/**
 * Custom exception class to handle cases where a user already exists.
 * Extends {@link RuntimeException}.
 */
public class UserAlreadyExistsException extends RuntimeException{

    /**
     * Custom exception class to handle cases where a user already exists.
     * Extends {@link RuntimeException}.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

