package com.workspark.certificateservice.exceptions.customExceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CertificateException extends RuntimeException{

    private final HttpStatus httpStatus;

    public CertificateException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CertificateException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}

