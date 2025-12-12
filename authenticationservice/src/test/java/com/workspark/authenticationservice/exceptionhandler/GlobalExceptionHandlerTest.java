package com.workspark.authenticationservice.exceptionhandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.workspark.authenticationservice.exceptions.customExceptions.AuthenticationFailedException;
import com.workspark.authenticationservice.exceptions.customExceptions.InvalidCredentialsException;
import com.workspark.authenticationservice.exceptions.customExceptions.PasswordResetException;
import com.workspark.authenticationservice.exceptions.customExceptions.RefreshTokenExpiredException;
import com.workspark.models.response.BaseRes;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

import com.workspark.authenticationservice.exceptions.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private org.slf4j.Logger log;

    @Test
    void handleValidationExceptions_ConstraintViolationException() {
        // Arrange
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        Path path1 = mock(Path.class);

        when(violation1.getPropertyPath()).thenReturn(path1);
        when(violation1.getMessage()).thenReturn("must not be null");
        when(path1.toString()).thenReturn("name");

        Set<ConstraintViolation<?>> violations = Set.of(violation1);
        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", violations);

        // Act
        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("name: must not be null", response.getBody().getMessage());
    }

    @Test
    void handleValidationExceptions_MethodArgumentNotValidException() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = new FieldError("object", "field1", "must not be empty");
        FieldError error2 = new FieldError("object", "field2", "must be a valid email");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));

        // Act
        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("field1: must not be empty, field2: must be a valid email", response.getBody().getMessage());
    }

    @Test
    void handleValidationExceptions_UnexpectedException() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody().getMessage());
    }


    /**
     * Test case for handling InvalidCredentialsException. Verifies that the
     * response status is UNAUTHORIZED and the error message is correctly returned.
     */
    @Test
    void handleInvalidCredentialsException() {
        // Arrange
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        // Act
        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleInvalidCredentialsException(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * Test case for handling RefreshTokenExpiredException. Verifies that the
     * response status is FORBIDDEN and the error message is correctly returned.
     */
    @Test
    void handleRefreshTokenExpired() {
        // Arrange
        RefreshTokenExpiredException exception = new RefreshTokenExpiredException("Refresh token expired");

        // Act
        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleRefreshTokenExpired(exception);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test case for handling PasswordResetException. Verifies that the response
     * status is BAD_REQUEST and the error message is correctly returned.
     */
    @Test
    void testHandlePasswordResetException() {
        PasswordResetException ex = new PasswordResetException("Password reset failed");

        ResponseEntity<BaseRes<String>> response = exceptionHandler.handlePasswordResetException(ex);

        // Assert that the status is BAD_REQUEST and the message is correct
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test case for handling IllegalArgumentException. Verifies that the response
     * status is BAD_REQUEST and the error message is correctly returned.
     */
    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleIllegalArgumentException(ex);

        // Assert that the status is BAD_REQUEST and the message is correct
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test case for handling a general exception. Verifies that the response status
     * is INTERNAL_SERVER_ERROR and the error message is correctly returned.
     */
    @Test
    void testHandleGeneralException() {
        Exception ex = new Exception("Internal server error");

        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleGeneralException(ex);

        // Assert that the status is INTERNAL_SERVER_ERROR and the message is correct
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Test case for handling AuthenticationFailedException. Verifies that the
     * response status is UNAUTHORIZED and the error message is correctly returned.
     */
    @Test
    void testHandleAuthenticationFailedException() {
        AuthenticationFailedException ex = new AuthenticationFailedException("Authentication failed");

        ResponseEntity<BaseRes<String>> response = exceptionHandler.handleAuthenticationFailedException(ex);

        // Assert that the status is UNAUTHORIZED and the message is correct
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
