package com.workspark.userservice.controller.controllerImpl;

import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import com.workspark.models.response.SignInResponse;
import com.workspark.userservice.controller.TenantUserController;
import com.workspark.userservice.model.dto.TenantUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.workspark.userservice.service.TenantUserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TenantUserControllerImpl implements TenantUserController {

    private final TenantUserService tenantUserService;

    /**
     * Retrieves a tenant user's details based on a key-value pair.
     */
    @Override
    public ResponseEntity<BaseRes<SignInResponse>> getUser(String key, String value) {
        log.info("Received request to get user with key: {} and value: {}", key, value);

        SignInResponse response = tenantUserService.getUserDetails(key, value);

        log.info("Successfully fetched user details for key: {} and value: {}", key, value);
        return BaseRes.success(response, "User details retrieved successfully.", HttpStatus.OK);
    }

    /**
     * Retrieves a tenant user by ID.
     */
    @Override
    public ResponseEntity<BaseRes<TenantUserDto>> getUserById(Long id) {
        log.info("Fetching user details for ID: {}", id);

        TenantUserDto response = tenantUserService.getUserDetailsById(id);

        log.info("User details retrieved successfully for ID: {}", id);
        return BaseRes.success(response, "User details retrieved successfully.", HttpStatus.OK);
    }

    /**
     * Loads user details for authentication.
     */
    @Override
    public ResponseEntity<BaseRes<SignInResponse>> loadUserForAuthentication(String key, String value) {
        log.info("Loading user for authentication with key: {} and value: {}", key, value);

        SignInResponse response = tenantUserService.getUserDetails(key, value);

        log.info("User authentication details loaded successfully for key: {}", key);
        return BaseRes.success(response, "User authentication data retrieved.", HttpStatus.OK);
    }

    /**
     * Adds a new tenant user.
     */
    @Override
    public ResponseEntity<BaseRes<String>> addTenantUser(SignupRequest request) {
        log.info("Starting signup process for user: {}", request.getEmail());

        tenantUserService.addTenantUser(request);

        log.info("Signup successful for user: {}", request.getEmail());
        return BaseRes.success("User Signup successful", "User created successfully.", HttpStatus.OK);
    }

    /**
     * Retrieves all tenant users with pagination.
     */
    @Override
    public ResponseEntity<BaseRes<PaginatedRes<SignInResponse>>> getAllTenantUsers(int page, int size) {
        log.info("Fetching all tenant users with pagination: page={}, size={}", page, size);

        PaginatedRes<SignInResponse> response = tenantUserService.getAllUsers(page, size);

        log.info("Successfully retrieved {} users from page {}", response.getData().size(), page);
        return BaseRes.success(response, "Tenant users retrieved successfully.", HttpStatus.OK);
    }

    /**
     * Updates an existing tenant user's details.
     */
    @Override
    public ResponseEntity<BaseRes<SignInResponse>> updateTenantUser(SignupRequest request) {
        log.info("Updating user: {}", request.getEmail());

        SignInResponse response = tenantUserService.updateTenantUser(request);

        log.info("User details updated successfully for user: {}", request.getEmail());
        return BaseRes.success(response, "User updated successfully.", HttpStatus.OK);
    }

    /**
     * Deactivates a tenant user.
     */
    @Override
    public ResponseEntity<BaseRes<SignInResponse>> deactivateUser(SignupRequest request) {
        log.info("Received request to deactivate user: {}", request.getEmail());

        SignInResponse response = tenantUserService.deactivateTenantUser(request);

        log.info("User deactivated successfully: {}", request.getEmail());
        return BaseRes.success(response, "User deactivated successfully.", HttpStatus.OK);
    }
}
