//package com.workspark.authenticationservice.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.mockStatic;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.UUID;
//
//import com.workspark.models.response.SignInResponse;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.workspark.authenticationservice.client.UserServiceClient;
//import com.workspark.authenticationservice.constant.Constants;
//import com.workspark.authenticationservice.exceptions.customExceptions.AuthenticationFailedException;
//import com.workspark.authenticationservice.exceptions.customExceptions.InvalidCredentialsException;
//import com.workspark.authenticationservice.exceptions.customExceptions.PasswordResetException;
//import com.workspark.authenticationservice.exceptions.customExceptions.RedisConnectionException;
//import com.workspark.authenticationservice.exceptions.customExceptions.RefreshTokenExpiredException;
//import com.workspark.authenticationservice.model.AuthenticationResponse;
//import com.workspark.authenticationservice.model.ChangePasswordRequest;
//import com.workspark.authenticationservice.model.SignInRequest;
//import com.workspark.authenticationservice.util.JwtUtil;
//import com.workspark.commonconfig.models.entity.RedisAuthUser;
//import com.workspark.commonconfig.service.RedisAuthUserService;
//import com.workspark.models.request.SignupRequest;
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceJWTImplTest {
//
//	@Mock
//	private JwtUtil jwtUtil;
//
//	@Mock
//	private UserServiceClient userServiceClient;
//
//	@Mock
//	private PasswordEncoder passwordEncoder;
//
//	@Mock
//	private RedisAuthUserService redisAuthUserService;
//
//	@InjectMocks
//	private AuthServiceJWTImpl authService;
//
//	private static final String TEST_EMAIL = "test@example.com";
//	private static final String OLD_PASSWORD = "oldPassword123";
//	private static final String NEW_PASSWORD = "newPassword123";
//	private static final String VALID_TOKEN = "valid.jwt.token";
//	private static final String USER_ID = "user123";
//
//	/**
//	 * Test for signUp method to verify that the correct user is created when a
//	 * valid request is provided.
//	 */
//	@Test
//	void testSignUp_whenValidRequest_Success() {
//		// Arrange
//		SignupRequest request = new SignupRequest();
//		request.setEmail("testuser@example.com");
//		request.setPassword("password123");
//		String tenantName = "tenant1";
//
//        SignupRequest userReq = new SignupRequest();
//        userReq.setEmail(request.getEmail());
//        userReq.setPassword(request.getPassword());
//
//		// Act
//		authService.signUp(request);
//
//		// Assert
//		// Verify that createUser method was called on userServiceClient with the
//		// correct arguments
//		verify(userServiceClient, times(1)).createUser(userReq);
//	}
//
//	/**
//	 * Test for signUp method to verify that caching occurs correctly when a valid
//	 * request is provided. We don't test the caching directly, but we verify the
//	 * method behavior.
//	 */
//	@Test
//	void testSignUp_whenValidRequest_Fail() {
//		// Arrange
//		SignupRequest request = new SignupRequest();
//		request.setEmail("testuser@example.com");
//		request.setPassword("password123");
//		String tenantName = "tenant1";
//
//		// Act
//		authService.signUp(request);
//
//        SignupRequest userReq = new SignupRequest();
//        userReq.setEmail(request.getEmail());
//        userReq.setPassword(request.getPassword());
//
//		// Assert
//		verify(userServiceClient).createUser(userReq);
//	}
//
//	/**
//	 * Test for signIn method to verify that the correct tokens are generated
//	 * when valid credentials are provided.
//	 */
//	@Test
//	void testSignIn_Success() {
//
//		// Arrange
//		String tenantName = "mindfire";
//		String rawPassword = "password";
//		SignInRequest request = new SignInRequest("test@example.com", rawPassword);
//
//		SignInResponse user = new SignInResponse();
//		user.setEmail("test@example.com");
//		String hashedPassword = passwordEncoder.encode(rawPassword); // Hash the password
//		user.setPassword(hashedPassword);
//
//		ResponseEntity<SignInResponse> response = ResponseEntity.ok(user);
//		when(userServiceClient.getUser("email", request.getEmail())).thenReturn(response);
//		when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
//
//		try (MockedStatic<UUID> utilities = mockStatic(UUID.class)) {
//			utilities.when(UUID::randomUUID).thenReturn(new UUID(5, 3));
//		}
//
//		// Act
//		AuthenticationResponse responseObj = authService.signIn(request);
//
//		// Assert
//		assertNotNull(responseObj);
//		verify(jwtUtil, times(1)).generateAccessToken(any(), anyString());
//		verify(jwtUtil, times(1)).generateRefreshToken(any(), anyString());
//		verify(redisAuthUserService, times(1)).saveUser(any());
//	}
//
//	/**
//	 * Test for signIn method to verify that the correct exception is thrown when
//	 * the provided password is invalid.
//	 */
//	@Test
//	void testSignIn_Fail_InvalidPassword() {
//		String tenantName = "mindfire";
//
//		// Arrange
//		SignInRequest request = new SignInRequest("test@example.com", "wrongPassword");
//        SignInResponse user = new SignInResponse();
//		user.setEmail("test@example.com");
//		user.setPassword("hashedPassword");
//
//		ResponseEntity<SignInResponse> response = ResponseEntity.ok(user);
//		when(userServiceClient.getUser("email", request.getEmail())).thenReturn(response);
//		when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);
//
//		// Act and Assert
//		assertThrows(AuthenticationFailedException.class, () -> authService.signIn(request));
//	}
//
//	/**
//	 * Test for signIn method to verify that the correct exception is thrown when
//	 * the provided email is not found in the database.
//	 */
//	@Test
//	void testSignIn_Fail_InvalidEmail() {
//		String tenantName = "mindfire";
//
//		// Arrange
//		SignInRequest request = new SignInRequest("test@example.com", "wrongPassword");
//        SignInResponse user = new SignInResponse();
//		user.setEmail("test@example.com");
//		user.setPassword("hashedPassword");
//
//		when(userServiceClient.getUser("email", request.getEmail())).thenReturn(null);
//
//		// Act and Assert
//		assertThrows(AuthenticationFailedException.class, () -> authService.signIn(request));
//	}
//
//	/**
//	 * Test case for refreshing the token when the refresh token is null. Verifies
//	 * that an IllegalArgumentException is thrown with the correct message.
//	 */
//	@Test
//	void testRefreshToken_shouldThrowException_whenRefreshTokenIsNull() {
//		// Act & Assert
//		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//			authService.refreshToken(null);
//		});
//
//		assertEquals(Constants.REFRESH_TOKEN_IS_REQUIRED, exception.getMessage());
//	}
//
//	/**
//	 * Test case for refreshing the token when the refresh token is empty. Verifies
//	 * that an IllegalArgumentException is thrown with the correct message.
//	 */
//	@Test
//	void testRefreshToken_shouldThrowException_whenRefreshTokenIsEmpty() {
//		// Act & Assert
//		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//			authService.refreshToken("");
//		});
//
//		assertEquals(Constants.REFRESH_TOKEN_IS_REQUIRED, exception.getMessage());
//	}
//
//	/**
//	 * Test case for refreshing the token when the refresh token is expired.
//	 * Verifies that a RefreshTokenExpiredException is thrown.
//	 */
//	@Test
//	void testRefreshToken_shouldThrowException_whenTokenNotValidated() {
//		String refreshToken = "expiredToken";
//		when(jwtUtil.validateToken(refreshToken, "userRefreshToken")).thenReturn(false);
//
//		// Act & Assert
//		RefreshTokenExpiredException exception = assertThrows(RefreshTokenExpiredException.class, () -> {
//			authService.refreshToken(refreshToken);
//		});
//
//		assertEquals(Constants.REFRESH_TOKEN_EXPIRED, exception.getMessage());
//	}
//
//	/**
//	 * Test case for refreshing the token when the token is valid. Verifies that a
//	 * new JWT and refresh token are returned.
//	 */
//	@Test
//	void testRefreshToken_shouldReturnNewToken_whenTokenIsValid() {
//		String refreshToken = "validToken";
//		String newJwtToken = "newJwtToken";
//		String newRefreshToken = "newRefreshToken";
//
//		when(jwtUtil.validateToken(refreshToken, "userRefreshToken")).thenReturn(true);
//		when(jwtUtil.generateNewTokenFromOldToken(refreshToken)).thenReturn(newJwtToken);
//		when(jwtUtil.generateNewRefreshTokenFromOldToken(refreshToken)).thenReturn(newRefreshToken);
//
//		// Act
//		AuthenticationResponse response = authService.refreshToken(refreshToken);
//
//		// Assert
//		assertNotNull(response);
//		assertEquals(newJwtToken, response.getJwtToken());
//		assertEquals(newRefreshToken, response.getRefreshToken());
//
//		// Verify that the necessary methods were called
//		verify(jwtUtil, times(1)).generateNewTokenFromOldToken(refreshToken);
//		verify(jwtUtil, times(1)).generateNewRefreshTokenFromOldToken(refreshToken);
//	}
//
//	/**
//	 * Test case for resetting the password successfully.
//	 */
//	@Test
//	void testResetPasswordSuccess() {
//		// Given
//		String resetToken = "valid-token";
//		String newPassword = "new-password";
//
//		// When
//		doNothing().when(userServiceClient).updatePassword(resetToken, newPassword); // Mocking success
//
//		// Then
//		assertDoesNotThrow(() -> authService.resetPassword(resetToken, newPassword)); // Should not throw exception
//		verify(userServiceClient, times(1)).updatePassword(resetToken, newPassword); // Ensure the client call is made
//	}
//
//	/**
//	 * Test case for resetting the password with a failure. Verifies that a
//	 * PasswordResetException is thrown when the password reset fails.
//	 */
//	@Test
//	void testResetPasswordFailure() {
//		// Given
//		String resetToken = "invalid-token";
//		String newPassword = "new-password";
//
//		// When
//		doThrow(new RuntimeException("User service failure")).when(userServiceClient).updatePassword(resetToken,
//				newPassword); // Mocking failure
//
//		// Then
//		PasswordResetException exception = assertThrows(PasswordResetException.class,
//				() -> authService.resetPassword(resetToken, newPassword)); // Should throw custom exception
//		assertEquals("Password reset failed due to an issue with the reset token or the request",
//				exception.getMessage());
//		verify(userServiceClient, times(1)).updatePassword(resetToken, newPassword); // Ensure the client call was made
//	}
//
//	/**
//	 * Test for changePassword method to verify that the password is updated when
//	 * the old password is correct.
//	 */
//	@Test
//	void changePassword_WhenOldPasswordCorrect_ShouldUpdatePassword() {
//		// Arrange
//		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(TEST_EMAIL, OLD_PASSWORD, NEW_PASSWORD);
//		when(userServiceClient.getPasswordByUsername(changePasswordRequest.getEmail())).thenReturn(OLD_PASSWORD);
//		when(passwordEncoder.matches(changePasswordRequest.getOldPassword(), OLD_PASSWORD)).thenReturn(true);
//
//		// Assert
//		assertDoesNotThrow(() -> authService.changePassword(changePasswordRequest));
//		verify(userServiceClient, times(1)).updatePassword(TEST_EMAIL, NEW_PASSWORD);
//	}
//
//	/**
//	 * Test for changePassword method to verify that the correct exception is thrown
//	 * when the old password is incorrect.
//	 */
//	@Test
//	void changePassword_WhenOldPasswordIncorrect_ShouldThrowException() {
//		// Arrange
//		String oldIncorrectPassword = "Random123";
//		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(TEST_EMAIL, OLD_PASSWORD, NEW_PASSWORD);
//		when(userServiceClient.getPasswordByUsername(changePasswordRequest.getEmail()))
//				.thenReturn(oldIncorrectPassword);
//
//		// Act & Assert
//		InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
//				() -> authService.changePassword(changePasswordRequest));
//		assertEquals(Constants.OLD_PASSWORD_IS_INCORRECT, exception.getMessage());
//		verify(userServiceClient, never()).updatePassword(any(), any());
//	}
//
//	/**
//	 * Test for validateToken method to verify that the correct user ID is extracted
//	 * from a valid token.
//	 */
//	@Test
//	void validateToken_WhenTokenValid_ShouldReturnRedisUserId() {
//		// Arrange
//		when(jwtUtil.validateToken(VALID_TOKEN, "userAccessToken")).thenReturn(true);
//		when(jwtUtil.extractRedisAuthUserId(VALID_TOKEN)).thenReturn(USER_ID);
//
//		// Act
//		String result = authService.validateToken(VALID_TOKEN);
//
//		// Assert
//		assertEquals(USER_ID, result);
//		verify(jwtUtil, times(1)).validateToken(VALID_TOKEN, "userAccessToken");
//		verify(jwtUtil, times(1)).extractRedisAuthUserId(VALID_TOKEN);
//	}
//
//	/**
//	 * Test for validateToken method to verify that the correct exception is thrown
//	 * when the token is invalid.
//	 */
//	@Test
//	void validateToken_WhenTokenInvalid_ShouldThrowException() {
//		// Arrange
//		when(jwtUtil.validateToken(VALID_TOKEN, "userAccessToken")).thenReturn(false);
//
//		// Act & Assert
//		AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class,
//				() -> authService.validateToken(VALID_TOKEN));
//		assertEquals(Constants.AUTHENTICATION_FAILED_USER_NOT_AUTHENTICATED, exception.getMessage());
//		verify(jwtUtil, never()).extractRedisAuthUserId(any());
//	}
//
//	@Test
//	void testSignIn_RedisConnectionFailure() {
//	    // Arrange
//	    SignInRequest request = new SignInRequest("test@example.com", "password");
//
//        SignInResponse user = new SignInResponse();
//	    user.setEmail("test@example.com");
//	    user.setPassword(passwordEncoder.encode("password"));  // Assuming passwordEncoder is mocked
//
//	    ResponseEntity<SignInResponse> responseEntity = ResponseEntity.ok(user);
//	    when(userServiceClient.getUser("email", request.getEmail())).thenReturn(responseEntity);
//	    when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
//
//	    // Mock Redis service to throw an exception when saveUser is called
//	    doThrow(new RuntimeException("Redis connection error")).when(redisAuthUserService).saveUser(any(RedisAuthUser.class));
//
//	    // Act & Assert
//	    RedisConnectionException exception = assertThrows(RedisConnectionException.class, () -> {
//	        authService.signIn(request);  // assuming authService is the instance of your service class
//	    });
//
//	    // Assert that the exception message is as expected
//	    assertEquals(Constants.UNABLE_TO_CONNECT_TO_REDIS, exception.getMessage());
//
//	}
//
//
//}
