package com.workspark.authservice.exceptions;

import com.workspark.authservice.exceptions.customExceptions.AuthenticationFailedException;
import com.workspark.authservice.exceptions.customExceptions.InvalidCredentialsException;
import com.workspark.authservice.exceptions.customExceptions.PasswordResetException;
import com.workspark.authservice.exceptions.customExceptions.RefreshTokenExpiredException;
import com.workspark.models.response.BaseRes;
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
 *
 * @author mridulj
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
	 * Handles InvalidCredentialsException.
	 */
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<BaseRes<String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		log.warn("Invalid credentials provided: {}", ex.getMessage());
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Handles RefreshTokenExpiredException.
	 */
	@ExceptionHandler(RefreshTokenExpiredException.class)
	public ResponseEntity<BaseRes<String>> handleRefreshTokenExpired(RefreshTokenExpiredException ex) {
		log.warn("Refresh token expired: {}", ex.getMessage());
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.FORBIDDEN);
	}

	/**
	 * Handles PasswordResetException.
	 */
	@ExceptionHandler(PasswordResetException.class)
	public ResponseEntity<BaseRes<String>> handlePasswordResetException(PasswordResetException ex) {
		log.error("Password reset error occurred: {}", ex.getMessage(), ex);
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.BAD_REQUEST);
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

	/**
	 * Handles AuthenticationFailedException.
	 */
	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<BaseRes<String>> handleAuthenticationFailedException(AuthenticationFailedException ex) {
		log.warn("Authentication failed: {}", ex.getMessage());
		return BaseRes.error(ex.getMessage(),ex.getClass().getSimpleName(),HttpStatus.UNAUTHORIZED);
	}
}
