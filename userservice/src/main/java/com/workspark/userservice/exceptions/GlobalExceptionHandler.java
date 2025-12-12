package com.workspark.userservice.exceptions;

import com.workspark.models.response.BaseRes;
import com.workspark.userservice.exceptions.customExceptions.RouteException;
import com.workspark.userservice.exceptions.customExceptions.TenantNotFoundException;
import com.workspark.userservice.exceptions.customExceptions.UserAlreadyExistsException;
import com.workspark.userservice.exceptions.customExceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global Exception Handler class for managing various exceptions thrown by the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
	public ResponseEntity<BaseRes<String>> handleValidationExceptions(Exception ex) {
		String errorMessage = switch (ex) {
			case ConstraintViolationException violationException ->
					violationException.getConstraintViolations()
							.stream()
							.map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
							.collect(Collectors.joining(", "));
			case MethodArgumentNotValidException methodArgumentException ->
					methodArgumentException.getBindingResult()
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
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles general uncaught exceptions.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseRes<String>> handleGeneralException(Exception ex) {
		log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(TenantNotFoundException.class)
	public ResponseEntity<BaseRes<String>> handleTenantNotFoundException(TenantNotFoundException ex) {
		log.error("Tenant not found exception: {}", ex.getMessage(), ex);
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<BaseRes<String>> handleUserNotFoundException(UserNotFoundException ex) {
		log.error("User not found exception: {}", ex.getMessage(), ex);
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<BaseRes<String>> handleUserNotFoundException(UserAlreadyExistsException ex) {
		log.error("User already exists exception: {}", ex.getMessage(), ex);
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RouteException.class)
	public ResponseEntity<BaseRes<String>> handleRouteException(RouteException ex) {
		log.error("Route exception: {}", ex.getMessage(), ex);
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
