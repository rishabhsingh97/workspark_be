package com.workspark.userservice.exceptions.customExceptions;

/**
 * Custom exception class to handle routing-related errors.
 * Extends {@link RuntimeException}.
 */
public class RouteException extends RuntimeException {

    /**
     * Constructor for creating a {@link RouteException} with a custom error message.
     *
     * @param message The error message describing the exception.
     */
    public RouteException(String message) {
        super(message);
    }
}
