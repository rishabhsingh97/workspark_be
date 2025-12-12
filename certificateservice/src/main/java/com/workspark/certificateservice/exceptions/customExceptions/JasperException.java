package com.workspark.certificateservice.exceptions.customExceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class JasperException extends RuntimeException{
    private final HttpStatus httpStatus;

    public JasperException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public JasperException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}

