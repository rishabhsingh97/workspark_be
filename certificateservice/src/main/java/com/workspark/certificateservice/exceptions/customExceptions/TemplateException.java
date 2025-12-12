package com.workspark.certificateservice.exceptions.customExceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class TemplateException extends RuntimeException{

    private final HttpStatus httpStatus;

    public TemplateException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public TemplateException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}

