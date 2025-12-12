package com.workspark.authenticationservice.exceptions.customExceptions;

public class PasswordResetException extends RuntimeException {
    public PasswordResetException(String message) {
        super(message);
    }
}