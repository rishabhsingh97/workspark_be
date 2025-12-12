package com.workspark.authenticationservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.workspark.authenticationservice.constant.Constants;
import com.workspark.authenticationservice.exceptions.GlobalExceptionHandler;
import com.workspark.authenticationservice.exceptions.customExceptions.AuthenticationFailedException;
import com.workspark.authenticationservice.exceptions.customExceptions.PasswordResetException;
import com.workspark.authenticationservice.exceptions.customExceptions.RefreshTokenExpiredException;
import com.workspark.authenticationservice.model.AuthenticationResponse;
import com.workspark.authenticationservice.model.ChangePasswordRequest;
import com.workspark.authenticationservice.model.SignInRequest;
import com.workspark.authenticationservice.service.IAuthService;
import com.workspark.authenticationservice.util.UtilityMethod;
import com.workspark.models.request.SignupRequest;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private AuthController authController; // The controller to test

	@Mock
	private IAuthService authService; // Mock the service layer
	@Mock
	private AuthenticationManager authenticationManager; // Mock AuthenticationManager

	private SignupRequest request;

	private SignInRequest signInRequest;

	/**
     * Initializes the necessary components before each test.
     * This method is executed before each test case and sets up the MockMvc object,
     * creates a valid signup request, and configures the controller to use the global exception handler.
     */
	@BeforeEach
	void setUp() {
		// Initialize MockMvc
		request = new SignupRequest();
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setEmail("john.doe@example.com");
		request.setPassword("password123");
		request.setPhoneNumber("1234567890");
		mockMvc = MockMvcBuilders.standaloneSetup(authController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	/**
     * Test case for user sign-up, ensuring successful registration with valid input.
     * This test simulates a valid signup request and checks that the status code
     * is 201 and the success message is returned.
     */
	@Test
     void testSignUpSuccess() throws Exception {

        // Mocking service call to signUp
        doNothing().when(authService).signUp(any(SignupRequest.class));

        // Act & Assert
        mockMvc.perform(post("/public/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(UtilityMethod.asJsonString(request))) // Use a utility method to convert request to JSON
                .andExpect(status().isCreated()) // Check that the response status is 201 (Created)
                .andExpect(jsonPath("$.message").value(Constants.SUCCESSFULLY_REGISTERED)) // Check response body message
                .andExpect(jsonPath("$.error").doesNotExist()); // Ensure no error in response body

        // Verify that the signUp method was called once
        verify(authService, times(1)).signUp(any(SignupRequest.class));
    }


	 /**
     * Test case for user sign-in, ensuring valid credentials result in a successful login.
     * The test simulates a sign-in with valid credentials and checks for successful
     * login with a JWT token and refresh token.
     */
	  @Test
	  void testSignIn_Success() throws Exception {

	        // Arrange
	        String tenantName = "testTenant";
	        signInRequest = new SignInRequest("testuser@gmail.com", "testpassword");
	        AuthenticationResponse authResponse = new AuthenticationResponse("jwtToken", "refreshToken");
	        when(authService.signIn(signInRequest)).thenReturn(authResponse);

	        // Act & Assert for positive scenario
	        mockMvc.perform(post("/public/signin")
	                .contentType(MediaType.APPLICATION_JSON)
	                .header("X-Tenant", tenantName)
	                .content(UtilityMethod.asJsonString(signInRequest))) // Convert SignInRequest to JSON
	                .andExpect(status().isOk()) // Expect HTTP 200 OK
	                .andExpect(jsonPath("$.item.jwtToken").value("jwtToken")) // Verify the access token
	                .andExpect(jsonPath("$.item.refreshToken").value("refreshToken")); // Verify the refresh token

	        // Verify that the signIn method was called once
	        verify(authService, times(1)).signIn(signInRequest);
	    }

	  /**
	     * Test case for user sign-in failure scenario, ensuring invalid credentials
	     * trigger an AuthenticationFailedException. The test checks for the expected
	     * error message and status code.
	     */
	    @Test
		void testSignIn_Failure() throws Exception {
	        // Arrange
	        String tenantName = "testTenant";
	        signInRequest = new SignInRequest("testuser@gmail.com", "testpassword");
	        when(authService.signIn(signInRequest)).thenThrow(new AuthenticationFailedException(Constants.INVALID_CREDENTIALS));

	        // Act & Assert for negative scenario
	        mockMvc.perform(post("/public/signin")
	                .contentType(MediaType.APPLICATION_JSON)
	                .header("X-Tenant", tenantName)
	                .content(UtilityMethod.asJsonString(signInRequest))) // Convert SignInRequest to JSON
	                .andExpect(status().isUnauthorized())
	                .andExpect(jsonPath("$.message").value(Constants.INVALID_CREDENTIALS));

	        // Verify that the signIn method was called once
	        verify(authService, times(1)).signIn(signInRequest);
	    }

	    /**
	     * Test case for password reset, ensuring that a successful password reset
	     * triggers the expected success message.
	     */
	    @Test
	    void testResetPassword_Success() throws Exception {
	        // Arrange
	        String resetToken = "resetToken";
	        String newPassword = "newPassword123";

	        // Mocking service call to resetPassword
	        doNothing().when(authService).resetPassword(anyString(), anyString());

	        // Act & Assert for positive scenario
	        mockMvc.perform(post("/public/reset")
	                .param("resetToken", resetToken)
	                .param("newPassword", newPassword)) // Pass resetToken and newPassword as query parameters
	                .andExpect(status().isOk()) // Expect HTTP 200 OK status
	                .andExpect(jsonPath("$.message").value("Password has been reset successfully")) // Verify success message
	                .andExpect(jsonPath("$.error").doesNotExist()); // Ensure no error field is present

	        // Verify that the resetPassword method was called once
	        verify(authService, times(1)).resetPassword(anyString(), anyString());
	    }

	    /**
	     * Test case for password reset failure scenario, simulating a failure in
	     * the password reset process and verifying the error message.
	     */
	    @Test
	    void testResetPassword_Failure() throws Exception {
	        // Arrange
	        String resetToken = "resetToken";
	        String newPassword = "newPassword123";

	        // Mocking service call to throw an exception (simulate failure)
	        doThrow(new PasswordResetException(Constants.PASSWORD_RESET_FAILED)).when(authService).resetPassword(anyString(), anyString());

	        // Act & Assert for failure scenario
	        mockMvc.perform(post("/public/reset")
	                .param("resetToken", resetToken)
	                .param("newPassword", newPassword)) // Pass resetToken and newPassword as query parameters
	                .andExpect(status().isBadRequest()) // Expect HTTP 500 due to failure
	                .andExpect(jsonPath("$.message").value(Constants.PASSWORD_RESET_FAILED));

	        // Verify that the resetPassword method was called once
	        verify(authService, times(1)).resetPassword(anyString(), anyString());
	    }

	    /**
	     * Test case for token validation, checking that a valid token returns a
	     * success message indicating the token is valid.
	     */
	@Test
	void testValidateToken_Success() throws Exception {
		// Arrange
		String tenantName = "testTenant";
		String token = "validToken";

		// Mocking service call
		when(authService.validateToken(token)).thenReturn("user123");

		// Act & Assert
		mockMvc.perform(get("/public/validate-token").header("X-Tenant", tenantName).param("token", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Token is valid"))
				.andExpect(jsonPath("$.success").value(true));

		verify(authService, times(1)).validateToken(anyString());
	}

	 /**
     * Test case for handling invalid token validation, ensuring that an invalid
     * token returns an error message indicating the token is invalid.
     */
	@Test
	void testValidateToken_Fail() throws Exception {
		// Arrange
		String tenantName = "testTenant";
		String token = "invalidToken";

		// Mocking service call
		when(authService.validateToken(token)).thenThrow(new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_USER_NOT_AUTHENTICATED));

		// Act & Assert
		mockMvc.perform(get("/public/validate-token").header("X-Tenant", tenantName).param("token", token))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value(Constants.AUTHENTICATION_FAILED_USER_NOT_AUTHENTICATED))
				.andExpect(jsonPath("$.success").value(false));

		verify(authService, times(1)).validateToken(anyString());
	}

	/**
     * Test case for refreshing a valid token, ensuring that a new JWT token
     * is returned upon successful refresh.
     */
	@Test
    void testRefreshToken_Success() throws Exception {
        // Arrange
        String refreshToken = "valid-refresh-token";
        // Mocking service call to refreshToken
        when(authService.refreshToken(anyString())).thenReturn(new AuthenticationResponse("newJwtToken", "newRefreshToken"));

        // Act & Assert for positive scenario
        mockMvc.perform(post("/public/refresh-token")
        		 .header("refresh-token", refreshToken)) // Pass the refreshToken as a query parameter
                .andExpect(status().isOk()) // Expect HTTP 200 OK status
                .andExpect(jsonPath("$.message").value(Constants.TOKEN_REFRESH_SUCCESSFULLY)) // Success message
                .andExpect(jsonPath("$.error").doesNotExist()) // No error field
                .andExpect(jsonPath("$.item.jwtToken").value("newJwtToken")) // Verify new JWT token
                .andExpect(jsonPath("$.item.refreshToken").value("newRefreshToken")); // Verify new refresh token

        // Verify that the refreshToken method was called once
        verify(authService, times(1)).refreshToken(anyString());
    }

	  /**
     * Test case for refreshing an invalid token, where the refresh token is expired.
     * The test simulates an error and verifies that the proper error response is returned.
     */
	@Test
	void testRefreshToken_Fail() throws Exception {
	    // Arrange
	    String refreshToken = "invalid-refresh-token";

	    // Mocking service call to throw an exception (simulate failure)
	    when(authService.refreshToken(anyString())).thenThrow(new RefreshTokenExpiredException(Constants.REFRESH_TOKEN_EXPIRED));

	    // Act & Assert for negative scenario
	    mockMvc.perform(post("/public/refresh-token")
	    		 .header("refresh-token", refreshToken)) // Pass refreshToken as a header
	            .andExpect(status().isForbidden());

	    // Verify that the refreshToken method was called once
	    verify(authService, times(1)).refreshToken(anyString());
	}

	/**
     * Test case for changing the password, ensuring the password change process
     * is successful with the expected response.
     */
	@Test
	void testChangePassword_Success() throws Exception {
	    // Arrange
	    String currentPassword = "currentPassword123";
	    String newPassword = "newPassword123";

	    // Create ChangePasswordRequest object with current and new passwords
	    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword, "tenantName");

	    // Mocking service call for changePassword
	    doNothing().when(authService).changePassword(any(ChangePasswordRequest.class));

	    // Act & Assert using MockMvc
	    mockMvc.perform(post("/public/change-password") // Assuming your endpoint is mapped as such
	            .contentType(MediaType.APPLICATION_JSON) // Ensure correct content type
	            .content(UtilityMethod.asJsonString(changePasswordRequest))) // Convert the request object to JSON
	            .andExpect(status().isOk()) // Expect HTTP 200 OK status
	            .andExpect(jsonPath("$.message").value(Constants.PASSWORD_CHANGED_SUCCESS)) // Success message
	            .andExpect(jsonPath("$.error").doesNotExist()); // Ensure error field does not exist

	    // Verify that the changePassword method was called once
	    verify(authService, times(1)).changePassword(any(ChangePasswordRequest.class));
	}

	 /**
     * Test case for failure during the password change process, simulating a server
     * error and checking the response for the error message.
     */
	@Test
	void testChangePassword_Fail() throws Exception {
	    // Arrange
	    String currentPassword = "currentPassword123";
	    String newPassword = "newPassword123";

	    // Create ChangePasswordRequest object
	    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword, "tenantName");

	    // Mocking service call for changePassword to throw an exception (simulate failure)
	    doThrow(new RuntimeException("Error changing password")).when(authService).changePassword(any(ChangePasswordRequest.class));

	    // Act & Assert for failure scenario
	    mockMvc.perform(post("/public/change-password") // Adjust endpoint as necessary
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(UtilityMethod.asJsonString(changePasswordRequest)))
	            .andExpect(status().isInternalServerError()) // Expect HTTP 500 Internal Server Error
	            .andExpect(jsonPath("$.message").value("Error changing password")); // Ensure error field exists

	    // Verify that the changePassword method was called once
	    verify(authService, times(1)).changePassword(any(ChangePasswordRequest.class));
	}
	
}
