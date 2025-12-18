package com.workspark.masterservice.controller;

import com.workspark.masterservice.model.dto.TenantDto;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.BasePageRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.workspark.masterservice.service.serviceImpl.TenantServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantServiceImpl tenantService;

    /**
     * Endpoint to create a new nomination.
     *
     * @param tenantDto The request data for creating the nomination.
     * @return ResponseEntity containing the NominationResponseDTO.
     */
    @PostMapping
    public ResponseEntity<BaseRes<TenantDto>> addTenant(@RequestBody TenantDto tenantDto) {

        log.info("Request received to create a nomination: {}", tenantDto);
        TenantDto savedTenantDto = tenantService.addTenant(tenantDto);
        log.info("Nomination created successfully with ID: {}", tenantDto.getId());
        return BaseRes.success(savedTenantDto,"", HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseRes<TenantDto>> getTenant(@PathVariable String id) {

        log.info("Request received to fetch nomination with ID: {}", id);
        TenantDto nomination = tenantService.getTenant(id);
        log.info("Nomination retrieved successfully: {}", nomination);
        return BaseRes.success(nomination, "", HttpStatus.OK);

    }

    /**
     * Endpoint to retrieve all nominations with pagination support.
     * 
     * @param page The page number for pagination (default is 0).
     * @param size The size of the page (default is 10).
     * @return ResponseEntity containing a paginated list of NominationResponseDTO.
     */
    @GetMapping
    public ResponseEntity<BasePageRes<TenantDto>> getAllTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Request received to fetch nominations with page: {} and size: {}", page, size);
        List<TenantDto> tenantDtoList = tenantService.getAllTenants(page, size);
        log.info("Number of nominations retrieved: {}", tenantDtoList.size());
        return BasePageRes.success(tenantDtoList, "", HttpStatus.OK);

    }

    /**
     * Endpoint to update an existing nomination.
     * 
     * @param id The ID of the nomination to update.
     * @param tenantDto The updated nomination details.
     * @return ResponseEntity containing the updated NominationResponseDTO.
     */
    @PutMapping("/{nominationId}")
    public ResponseEntity<BaseRes<TenantDto>> updateTenant(
            @PathVariable String id,
            @RequestBody TenantDto tenantDto) {

        log.info("Request received to update nomination with ID: {}", id);
        TenantDto updatedTenantDto = tenantService.updateTenant(id, tenantDto);
        log.info("Nomination updated successfully: {}", updatedTenantDto);
        return BaseRes.success(updatedTenantDto, "", HttpStatus.OK);

    }
}
