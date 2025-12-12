package com.workspark.userservice.exceptions.customExceptions;

/**
 * Custom exception class to handle cases where a tenant is not found.
 * Extends {@link RuntimeException}.
 */
public class TenantNotFoundException extends RuntimeException{

    /**
     * Custom exception class to handle cases where a tenant is not found.
     * Extends {@link RuntimeException}.
     */
    public TenantNotFoundException(String message) {
        super(message);
    }
}
