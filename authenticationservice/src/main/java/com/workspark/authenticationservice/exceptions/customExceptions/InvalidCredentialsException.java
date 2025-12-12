package com.workspark.authenticationservice.exceptions.customExceptions;

public class InvalidCredentialsException extends RuntimeException {

      public InvalidCredentialsException(String message) {
        super(message);
    }
}
