package com.workspark.authenticationservice.controller;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.workspark.commonconfig.service.RedisPubService;
import com.workspark.commonconfig.utils.TemplateLoader;
import com.workspark.models.pojo.Email;
import com.workspark.models.pojo.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workspark.authenticationservice.constant.Constants;
import com.workspark.authenticationservice.model.AuthenticationResponse;
import com.workspark.authenticationservice.model.ChangePasswordRequest;
import com.workspark.authenticationservice.model.SignInRequest;
import com.workspark.models.request.SignupRequest;
import com.workspark.authenticationservice.service.IAuthService;
import com.workspark.commonconfig.models.pojo.TenantContext;
import com.workspark.models.response.BaseRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for handling user authentication operations, including
 * sign-up, sign-in, and token refresh. This controller exposes endpoints for
 * user registration, logging in with JWT token generation, and refreshing
 * expired JWT tokens using a valid refresh token. It delegates the core
 * business logic to the AuthService.
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class AuthController {

    private final IAuthService authService;
    private final RedisPubService redisPubService;
    private final TemplateLoader templateLoader;
    /**
     * Registers a new user.
     *
     * @param request The user registration details.
     * @return A response indicating successful registration with a 201 status code.
     */
    @PostMapping("/signup")
    public ResponseEntity<BaseRes<String>> signUp(@Valid @RequestBody SignupRequest request) {
        authService.signUp(request);
        return BaseRes.success(Constants.SUCCESSFULLY_REGISTERED, Constants.SUCCESSFULLY_REGISTERED,
                HttpStatus.CREATED);
    }

    /**
     * Signs in an existing user and returns JWT tokens.
     *
     * @param request The sign-in request with user credentials.
     * @return A response containing the authentication tokens and a success
     * message.
     */
    @Operation(summary = "Sign in an existing user", description = "Authenticates a user and returns JWT tokens")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully signed in"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")})
    @PostMapping("/signin")
    public ResponseEntity<BaseRes<AuthenticationResponse>> signIn(@Valid @RequestBody SignInRequest request) throws JsonProcessingException {
        AuthenticationResponse authResponse = authService.signIn(request);
        try {
//            redisPubService.publish(
//                    Notification.builder().
//                            email(
//                                    Email.builder()
//                                            .fromEmail("rishabhsingh97it@gmail.com")
//                                            .fromName("Workspark Support")
//                                            .toEmail("rishthakur18@gmail.com")
//                                            .toName("Rishabh Singh")
//                                            .subject("Test Mail")
//                                            .template(templateLoader.readFileFromResources("abc.html"))
//                                            .parameters(Map.of("name", "John Doe",
//                                                    "hasSpecialOffer", true,
//                                                    "specialOfferMessage", "Enjoy 10% off your first purchase!",
//                                                    "verificationToken", "your_verification_token")
//                                            )
//                                            .message("Hi this is test mail")
//                                            .build()
//                            )
//                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return BaseRes.success(authResponse, Constants.SUCCESSFULLY_SIGNED_IN, HttpStatus.OK);
    }

    /**
     * Refreshes the JWT token using a valid refresh token.
     *
     * @param refreshToken The refresh token to generate a new JWT.
     * @return A response containing the new authentication tokens.
     */
    @Operation(summary = "Refresh the JWT token", description = "Generates a new JWT token using the provided refresh token.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Token successfully refreshed"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")})
    @PostMapping("/refresh-token")
    public ResponseEntity<BaseRes<AuthenticationResponse>> refreshToken(
            @RequestHeader("refresh-token") @Parameter(description = "The refresh token to generate a new JWT") String refreshToken) {
        AuthenticationResponse response = authService.refreshToken(refreshToken);
        return BaseRes.success(response, Constants.TOKEN_REFRESH_SUCCESSFULLY, HttpStatus.OK);
    }

    /**
     * Resets the user’s password using a valid reset token.
     *
     * @param resetToken  The valid reset token sent to the user.
     * @param newPassword The new password to set for the user.
     * @return A response indicating successful password reset.
     */
    @Operation(summary = "Reset user password", description = "Allows a user to reset their password using a valid reset token.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Password successfully reset"),
            @ApiResponse(responseCode = "400", description = "Invalid reset token or password")})
    @PostMapping("/reset")
    public ResponseEntity<BaseRes<String>> resetPassword(
            @Parameter(description = "The valid reset token sent to the user for password reset", required = true) @RequestParam String resetToken,
            @Parameter(description = "The new password the user wants to set", required = true) @RequestParam String newPassword) {

        authService.resetPassword(resetToken, newPassword);
        return BaseRes.success(Constants.PASSWORD_RESET_SUCCESSFULLY, Constants.PASSWORD_RESET_SUCCESSFULLY,
                HttpStatus.OK);
    }

    /**
     * Changes the user's password by providing the current and new passwords.
     *
     * @param changePasswordRequest The request containing the current and new
     *                              password.
     * @return A response indicating successful password change.
     */
    @Operation(summary = "Change user password", description = "Allows a user to change their password by providing the current password and new password.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Password successfully changed"),
            @ApiResponse(responseCode = "400", description = "Invalid current password or new password is invalid")})
    @PostMapping("/change-password")
    public ResponseEntity<BaseRes<String>> changePassword(
            @Parameter(description = "The request containing the current and new password for the user", required = true) @RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return BaseRes.success(Constants.PASSWORD_CHANGED_SUCCESS, Constants.PASSWORD_CHANGED_SUCCESS, HttpStatus.OK);
    }

    /**
     * Refreshes the JWT token using the provided refresh token.
     *
     * @param token The refresh token used to generate a new JWT token.
     * @return A ResponseEntity containing the new JWT and refresh token if
     * successful.
     */
    @Operation(summary = "Validate the JWT token", description = "Validate JWT token and fetch redis id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Token successfully validated"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")})
    @GetMapping("/validate-token")
    public ResponseEntity<BaseRes<String>> validateToken(@RequestParam String token) {

        // Attempt to validate the token using the service
        String apiResponse = authService.validateToken(token);
        return BaseRes.success(apiResponse, "Token is valid", HttpStatus.OK);
    }
}
