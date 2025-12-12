package com.workspark.userservice.exceptions.customExceptions;

/**
 * Custom exception class to handle cases where a user is not found.
 * Extends {@link RuntimeException}.
 */
public class UserNotFoundException extends RuntimeException{

    /**
     * Custom exception class to handle cases where a user is not found.
     * Extends {@link RuntimeException}.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
