package com.workspark.nominationservice.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * Centralized exception handling for specific and generic exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles NominationNotFoundException.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with NOT_FOUND status and the exception message
     */
    @ExceptionHandler(NominationNotFoundException.class)
    public ResponseEntity<String> handleNominationNotFoundException(NominationNotFoundException ex) {
        logger.error("NominationNotFoundException encountered: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    /**
     * Handles ResourceNotFoundException.
     * Used for cases where a requested resource is not found.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with NOT_FOUND (404) status and the exception message
     */

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
    /**
     * Handles IllegalArgumentException.
     * Triggered when an illegal or inappropriate argument is passed.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with BAD_REQUEST (400) status and the exception message
     */

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( ex.getMessage());
	}



    /**
     * Handles RecognitionCategoryNotFoundException.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with NOT_FOUND status and the exception message
     */
    @ExceptionHandler(RecognitionCategoryNotFoundException.class)
    public ResponseEntity<String> handleRecognitionCategoryNotFoundException(RecognitionCategoryNotFoundException ex) {
        logger.error("RecognitionCategoryNotFoundException encountered: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles NoRecordsFoundException.
     * Triggered when no records are found during a database or service call.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with NOT_FOUND (404) status and the exception message
     */

    @ExceptionHandler(NoRecordsFoundException.class)
    public ResponseEntity<String> handleNoRecordsFoundException(NoRecordsFoundException ex) {
        logger.error("NoRecordsFoundException encountered: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    /**
     * Handles QuestionNotFoundException.
     * Triggered when a specific question cannot be found in the system.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with NOT_FOUND (404) status and the exception message
     */

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<String> handleQuestionNotFoundException(QuestionNotFoundException ex) {
        logger.error("QuestionNotFoundException encountered: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


}
