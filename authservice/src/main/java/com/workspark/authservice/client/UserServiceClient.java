package com.workspark.authservice.client;

import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.SignInResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for interacting with the user service to create users, retrieve
 * passwords, and update passwords.
 * 
 * The base URL should be configured externally.
 * 
 * @author mridulj
 */
@FeignClient("USERSERVICE")
public interface UserServiceClient {

	/**
	 * Creates a new user in the user service by sending a POST request with the
	 * provided signup request.
	 *
	 * @param request The request containing user details to be saved.
	 */
	@PostMapping("user/api/v1/tenant")
	ResponseEntity<String> createUser(@RequestBody SignupRequest request);

	/**
	 * Retrieves the stored password for a user by their username by sending a GET
	 * request to the user service.
	 *
	 * @param  key username of the user whose password is to be retrieved.
	 * @return The password of the user.
	 */

	@GetMapping("user/api/v1/tenant-user/loadUserForAuthentication")
	BaseRes<SignInResponse> getUser(@RequestParam("key") String key, @RequestParam("value") String value);

	/**
	 * Retrieves the stored password for a user by their username by sending a GET
	 * request to the user service.
	 * 
	 * @param username The username of the user whose password is to be retrieved.
	 * @return The password of the user.
	 */

	@GetMapping("user/api/v1/password")
	BaseRes<String> getPasswordByUsername(String username);

	/**
	 * Updates a user's password by sending a POST request to the user service with
	 * a reset token and the new password.
	 *
	 * @param resetToken  The token that verifies the user's request to reset their
	 *                    password.
	 * @param newPassword The new password to be set for the user.
	 */
	@PostMapping("user/api/v1/password/update")
	void updatePassword(@RequestParam("resetToken") String resetToken, @RequestParam("newPassword") String newPassword);

}