package com.workspark.masterservice.controller;

import com.workspark.masterservice.service.LiquibaseService;
import com.workspark.models.response.BaseRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/migration")
@RequiredArgsConstructor
public class MigrationController {

    private final LiquibaseService runMigration;

    /**
     * Endpoint to create a new nomination.
     *
     * @param tenantDto The request data for creating the nomination.
     * @return ResponseEntity containing the NominationResponseDTO.
     */
    @PostMapping("/run")
    public ResponseEntity<BaseRes<String>> runMigration() {
        return BaseRes.success("","", HttpStatus.CREATED);
    }
}
