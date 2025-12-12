package com.workspark.notificationservice.exceptions;

import com.workspark.models.response.BaseRes;
import com.workspark.notificationservice.exceptions.customExceptions.EmailException;
import com.workspark.notificationservice.exceptions.customExceptions.NotificationException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

/**
 * Global Exception Handler class for managing various exceptions thrown by the application.
 *
 * @author mridulj
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<BaseRes<String>> handleValidationExceptions(Exception ex) {
        String errorMessage = switch (ex) {
            case ConstraintViolationException violationException -> violationException.getConstraintViolations()
                    .stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            case MethodArgumentNotValidException methodArgumentException -> methodArgumentException.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            default -> "Validation error";
        };
        log.error("Validation error: {}", errorMessage);
        return BaseRes.error(errorMessage, "Validation Error", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseRes<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument exception: {}", ex.getMessage(), ex);
        return BaseRes.error(ex.getMessage(), ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HandlerMethodValidationException.
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<BaseRes<String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        log.error("Validation exception: {}", ex.getMessage(), ex);
        return BaseRes.error(ex.getMessage(), ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general uncaught exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseRes<String>> handleGeneralException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        return BaseRes.error(ex.getMessage(), ex.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Custom Exceptions START

    /**
     * Handles TemplateException.
     */
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<BaseRes<String>> handleTemplateException(EmailException ex) {
        log.error("Email exception: {}", ex.getMessage(), ex);
        return BaseRes.error(ex.getMessage(), ex.getClass().getSimpleName(), ex.getHttpStatus());
    }

    /**
     * Handles JasperException.
     */
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<BaseRes<String>> handleJasperException(NotificationException ex) {
        log.error("Notification exception: {}", ex.getMessage(), ex);
        return BaseRes.error(ex.getMessage(), ex.getClass().getSimpleName(), ex.getHttpStatus());
    }

}
