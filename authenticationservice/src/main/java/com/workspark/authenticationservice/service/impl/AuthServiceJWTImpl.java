package com.workspark.authenticationservice.service.impl;

import java.util.Objects;
import java.util.UUID;

import com.workspark.models.response.BaseRes;
import com.workspark.models.response.SignInResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workspark.authenticationservice.client.UserServiceClient;
import com.workspark.authenticationservice.constant.Constants;
import com.workspark.authenticationservice.exceptions.customExceptions.AuthenticationFailedException;
import com.workspark.authenticationservice.exceptions.customExceptions.InvalidCredentialsException;
import com.workspark.authenticationservice.exceptions.customExceptions.PasswordResetException;
import com.workspark.authenticationservice.exceptions.customExceptions.RedisConnectionException;
import com.workspark.authenticationservice.exceptions.customExceptions.RefreshTokenExpiredException;
import com.workspark.authenticationservice.model.AuthenticationResponse;
import com.workspark.authenticationservice.model.ChangePasswordRequest;
import com.workspark.authenticationservice.model.SignInRequest;
import com.workspark.authenticationservice.service.IAuthService;
import com.workspark.authenticationservice.util.JwtUtil;
import com.workspark.commonconfig.models.entity.RedisAuthUser;
import com.workspark.commonconfig.service.RedisAuthUserService;
import com.workspark.models.pojo.AuthUser;
import com.workspark.models.request.SignupRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the AuthService interface that handles user authentication
 * operations, including sign-up, sign-in, and token refresh. It manages user
 * registration, authentication via JWT, and refresh token generation. It also
 * interacts with external services for user creation and password validation.
 *
 * @author mridulj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceJWTImpl implements IAuthService {

	private final JwtUtil jwtUtil;
	private final UserServiceClient userServiceClient;
	private final PasswordEncoder passwordEncoder;
	private final RedisAuthUserService redisAuthUserService;

	/*
	 * Registers a new user
	 *
	 * @param request Contains the user details (username, password).
	 */
	@Override
	public void signUp(SignupRequest request) {
		log.info("Starting sign up process for username: {}", request.getEmail());

		SignupRequest userReq = SignupRequest.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(request.getPassword())
				.phoneNumber(request.getPhoneNumber())
//				.roles(req.getRoles())
				.build();

		// Call the user service client to create the user
		userServiceClient.createUser(userReq);
		log.info("User {} successfully registered", request.getEmail());
	}

	/**
	 * Signs in a user by validating their credentials, generating JWT tokens, and
	 * storing user details in Redis for session tracking.
	 *
	 * @param request Contains the sign-in credentials (username, password).
	 * @return AuthenticationResponse containing the generated JWT and refresh
	 *         tokens.
	 */
	@Override
	public AuthenticationResponse signIn(SignInRequest request) {
		log.info("Attempting to sign in user: {}", request.getEmail());

		// Fetch user details from user service
		BaseRes<SignInResponse> responseEntity = userServiceClient.getUser("email",
				request.getEmail());

		// Check if the response is valid and OK
		if (Objects.nonNull(responseEntity)
				&& responseEntity.isSuccess()) {

			SignInResponse a = responseEntity.getItem();
			if(Objects.isNull(a)){
				throw new InvalidCredentialsException("User not found or invalid credentials.");
			}

            String jwtToken;
			String refreshToken;

			if (passwordEncoder.matches(request.getPassword(), a.getPassword())) {

				try {

					// Generate a UUID key for storing user details in Redis
					String userKey = UUID.randomUUID().toString();

					AuthUser authUser = AuthUser.builder()
							.id(a.getUserId())
							.email(a.getEmail())
							.firstName(a.getFirstName())
							.lastName(a.getLastName())
							.phoneNumber(a.getPhoneNumber())
							.roles(a.getRoles())
							.build();

					// Generate JWT and refresh tokens based on the email
					jwtToken = jwtUtil.generateAccessToken(authUser, userKey);
					refreshToken = jwtUtil.generateRefreshToken(authUser,userKey);

					RedisAuthUser redisAuthUser = RedisAuthUser.builder()
							.redisUserId(userKey)
							.authUser(authUser)
							.build();

					// Store user details in Redis with a TTL of 1 hour
					redisAuthUserService.saveUser(redisAuthUser);
					log.info("User {} successfully signed in and details stored in Redis",
							a.getEmail() + " " + a.getFirstName());
				} catch (Exception e) {
					log.error("Unable to connect to Redis: {}", e.getMessage());
					throw new RedisConnectionException(Constants.UNABLE_TO_CONNECT_TO_REDIS);
				}

				// Return the authentication response with JWT and refresh token
				return new AuthenticationResponse(jwtToken, refreshToken);
			} else {
				// Handle case where user body is null
				log.error("User details could not be retrieved for email: {}", request.getEmail());
				throw new AuthenticationFailedException("User not found or invalid credentials.");
			}
		} else {
			// Handle failed response from user service
			log.error("Failed to retrieve user for email: {}. Response: {}", request.getEmail(),
                    Objects.nonNull(responseEntity) ? "" : "No response");
			throw new AuthenticationFailedException("User service is unavailable or invalid credentials.");
		}
	}


	/**
	 * Refreshes the JWT token by validating the provided refresh token and
	 * generating a new JWT and refresh token.
	 *
	 * @param refreshToken The refresh token to generate new JWT and refresh token.
	 * @return AuthenticationResponse containing the new JWT and refresh tokens.
	 * @throws RefreshTokenExpiredException If the provided refresh token is
	 *                                      expired.
	 */
	@Override
	public AuthenticationResponse refreshToken(String refreshToken) {
		log.info("Refreshing token for provided refresh token");

		// Validate that the refresh token is not null or empty
		if (Objects.isNull(refreshToken) || refreshToken.isEmpty()) {
			log.error("Refresh token is required but not provided");
			throw new IllegalArgumentException(Constants.REFRESH_TOKEN_IS_REQUIRED);
		}

		// Check if the refresh token is expired
		if (!jwtUtil.validateToken(refreshToken, "userRefreshToken")) {
			throw new RefreshTokenExpiredException(Constants.REFRESH_TOKEN_EXPIRED);
		}

		// Generate new JWT and refresh token
		String newJwtToken = jwtUtil.generateNewTokenFromOldToken(refreshToken);
		String newRefreshToken = jwtUtil.generateNewRefreshTokenFromOldToken(refreshToken);

		return new AuthenticationResponse(newJwtToken, newRefreshToken);
	}

	/**
	 * Resets the user's password by calling the `UserServiceClient` to update the
	 * password using the provided reset token and new password.
	 *
	 * @param resetToken  The token used to verify the user's password reset
	 *                    request.
	 * @param newPassword The new password to set for the user.
	 */
	@Override
	public void resetPassword(String resetToken, String newPassword) {
		log.info("Resetting password for user with reset token: {}", resetToken);
		try {
			userServiceClient.updatePassword(resetToken, newPassword);
			log.info("Password reset successful for reset token: {}", resetToken);
		} catch (Exception ex) {
			log.error("Failed to reset password for reset token: {}", resetToken, ex);
			throw new PasswordResetException(Constants.PASSWORD_RESET_FAILED);

		}
	}

	/**
	 * Handles the business logic of changing the user's password.
	 *
	 * @param changePasswordRequest The request containing the old and new password.
	 */
	public void changePassword(ChangePasswordRequest changePasswordRequest) {
		log.info("Changing password for user: {}", changePasswordRequest.getEmail());
		BaseRes<String> currentPasswordRes = userServiceClient.getPasswordByUsername(changePasswordRequest.getEmail());
		String currentPassword = currentPasswordRes.getItem();
		// Validate if the old password matches
		if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), currentPassword)) {
			log.error("Incorrect old password for user: {}", changePasswordRequest.getEmail());
			throw new InvalidCredentialsException(Constants.OLD_PASSWORD_IS_INCORRECT);
		}

		userServiceClient.updatePassword(changePasswordRequest.getEmail(), changePasswordRequest.getNewPassword());
		log.info("Password successfully changed for user: {}", changePasswordRequest.getEmail());

	}

	/**
	 * Validates the given JWT token and extracts the user ID.
	 *
	 * @param token The JWT token to validate.
	 * @return ApiResponce An object containing the token, validity status, and
	 *         extracted user ID.
	 *                                processed.
	 */
	@Override
	public String validateToken(String token) {
		if(jwtUtil.validateToken(token, "userAccessToken")){
			return jwtUtil.extractRedisAuthUserId(token);
		}
		else throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_USER_NOT_AUTHENTICATED);
	}


}
