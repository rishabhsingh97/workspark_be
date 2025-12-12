package com.workspark.userservice.controller;

import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BaseRes;
import com.workspark.userservice.model.dto.response.TenantResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Interface defining the Tenant-related operations exposed via HTTP.
 * This includes actions like adding a tenant admin.
 */
@RequestMapping("/api/v1/tenant")
public interface TenantController {

    /**
     * Adds a new tenant admin to the system.
     *
     * This endpoint receives a {@link SignupRequest} containing the details of the tenant admin
     * to be created, processes the information, and responds with a confirmation message.
     *
     * @param request The {@link SignupRequest} object containing the tenant admin's details,
     *                such as username, password, and other relevant information.
     * @return A {@link ResponseEntity} containing a confirmation message in the body
     *         and an HTTP status code indicating the result of the operation.
     * @throws Exception If there is an error during the process, such as invalid input data
     *                   or failure to create the tenant admin in the system.
     *
     * @see SignupRequest for the structure of the data required to add the tenant admin.
     */
    @PostMapping
    ResponseEntity<BaseRes<String>> addTenantAdmin(@RequestBody SignupRequest request);

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<TenantResponse>> getTenant() ;


}
