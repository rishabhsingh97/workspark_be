package com.workspark.authservice.exceptions.customExceptions;

public class InvalidCredentialsException extends RuntimeException {

      public InvalidCredentialsException(String message) {
        super(message);
    }
}
