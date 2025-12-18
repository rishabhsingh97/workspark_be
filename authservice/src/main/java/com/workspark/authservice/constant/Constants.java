package com.workspark.authservice.constant;

public class Constants {

	// Success messages
	public static final String SUCCESSFULLY_REGISTERED = "User has been registered successfully";
	public static final String SUCCESSFULLY_SIGNED_IN = "User has been signed in successfully";
	public static final String PASSWORD_RESET_SUCCESSFULLY = "Password has been reset successfully";
	public static final String PASSWORD_CHANGED_SUCCESS = "Password has been changed successfully";
	public static final String TOKEN_REFRESH_SUCCESSFULLY = "Token has been refreshed successfully";

	// Error messages
	public static final String PASSWORD_IS_REQUIRED = "Password is required";
	public static final String INVALID_CREDENTIALS = "The credentials provided are invalid";
	public static final String REFRESH_TOKEN_IS_REQUIRED = "A refresh token is required";
	public static final String REFRESH_TOKEN_EXPIRED = "The refresh token has expired";
	public static final String USERNAME_IS_ALREADY_TAKEN = "This username is already taken";
	public static final String PHONE_IS_ALREADY_TAKEN = "This Phone is already taken";
	public static final String UNABLE_TO_CONNECT_TO_REDIS = "Unable to connect to Redis";
	public static final String AUTHENTICATION_FAILED_USER_NOT_AUTHENTICATED = "Authentication failed: User could not be authenticated.";
	public static final String AUTHENTICATION_FAILED_DUE_TO_INTERNAL_ERROR = "Authentication failed due to an internal error: ";
	public static final String PASSWORD_RESET_FAILED = "Password reset failed due to an issue with the reset token or the request";
	public static final String OLD_PASSWORD_IS_INCORRECT = "Old password is incorrect";
	public static final String USER_NOT_FOUND_OR_INVALID_CREDENTIALS = "User not found or Invalid credentials.";
	public static final String USER_SERVICE_IS_UNAVAILABLE_OR_INVALID_CREDENTIALS = "User service is unavailable or invalid credentials.";

	// Fields
	public static final String EMAIL = "email";

	// Swagger
	public static final String SWAGGER_TITLE = "Auth Service API";
	public static final String SWAGGER_VERSION = "1.0";
	public static final String SWAGGER_DESCRIPTION = "API documentation for Auth Service";

	// Private constructor to prevent instantiation
	private Constants() {
	}
}