package com.workspark.userservice.controller.controllerImpl;

import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BaseRes;
import com.workspark.userservice.controller.TenantController;
import com.workspark.userservice.model.dto.response.TenantResponse;
import com.workspark.userservice.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementation of the {@link TenantController} interface.
 * This class handles HTTP requests related to tenant management, such as adding a tenant admin.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TenantControllerImpl implements TenantController {

    @Autowired
    private TenantService tenantService;

    /**
     * Adds a new tenant admin to the system.
     * <p>
     * This method receives a {@link SignupRequest} containing the details of the tenant admin to be created.
     * It calls the {@link TenantService#addTenantAdmin(SignupRequest)} method to process the request and
     * returns a success message if the operation is successful.
     *
     * @param request The {@link SignupRequest} object containing the tenant admin's details, such as username,
     *                password, and other required information.
     * @return A {@link ResponseEntity} with a success message if the tenant admin is successfully created.
     * @throws Exception If there is an error during the process, such as invalid input or failure to create the tenant admin.
     */
    @Override
    public ResponseEntity<BaseRes<String>> addTenantAdmin(SignupRequest request) {
        tenantService.addTenantAdmin(request);
        return BaseRes.success("User Signup successful", "", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TenantResponse>> getTenant() {
      //  log.info("Fetching all recognition categories");
        List<TenantResponse> response = tenantService.getAllTenant();
        log.info("All recognition categories fetched successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
