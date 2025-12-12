package com.workspark.userservice.controller;

import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import com.workspark.models.response.SignInResponse;
import com.workspark.userservice.model.dto.TenantUserDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/tenant-user")
public interface TenantUserController {

    /**
     * Adds a new tenant user.
     *
     * @param request The {@link SignupRequest} containing user details.
     * @return A success message if the user is successfully created.
     */
    @PostMapping
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    ResponseEntity<BaseRes<String>> addTenantUser(@Valid @RequestBody SignupRequest request);

    /**
     * Retrieves all tenant users.
     *
     * @return A list of {@link SignInResponse} containing tenant user details.
     */
    @GetMapping
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    ResponseEntity<BaseRes<PaginatedRes<SignInResponse>>> getAllTenantUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    );

    /**
     * Retrieves tenant users by id.
     *
     * @return A list of {@link SignInResponse} containing tenant user details.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    ResponseEntity<BaseRes<TenantUserDto>> getUserById(@PathVariable Long id);

    /**
     * Updates an existing tenant user's details.
     *
     * @param request The {@link SignupRequest} containing updated user information.
     * @return A success message if the user is successfully updated.
     */
    @PutMapping
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    ResponseEntity<BaseRes<SignInResponse>>  updateTenantUser(@RequestBody SignupRequest request);


    /**
     * Updates an existing tenant user's details.
     *
     * @param request The {@link SignupRequest} containing updated user information.
     * @return A success message if the user is successfully updated.
     */
    @DeleteMapping
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    ResponseEntity<BaseRes<SignInResponse>>  deactivateUser(@RequestBody SignupRequest request);

    /**
     * Retrieves a tenant user's details based on a specified key-value pair.
     *
     * @param key   The key to search for (e.g., email, userId).
     * @param value The value of the key to match.
     * @return The {@link SignInResponse} containing the user's details.
     */
    @GetMapping("/getUserDetails")
    @PreAuthorize("hasRole('TENANT_ADMIN') or hasRole('SYSTEM')")
    ResponseEntity<BaseRes<SignInResponse>> getUser(@RequestParam("key") String key, @RequestParam("value") String value);

    /**
     * Retrieves a tenant user's details based on a specified key-value pair.
     *
     * @param key   The key to search for (e.g., email, userId).
     * @param value The value of the key to match.
     * @return The {@link SignInResponse} containing the user's details.
     */
    @GetMapping("/loadUserForAuthentication")
    ResponseEntity<BaseRes<SignInResponse>> loadUserForAuthentication(@RequestParam("key") String key, @RequestParam("value") String value);

}
