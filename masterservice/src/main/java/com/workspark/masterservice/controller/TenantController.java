package com.workspark.masterservice.controller;

import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.workspark.masterservice.dto.NominationRequestDTO;
import com.workspark.masterservice.dto.NominationResponseDTO;
import com.workspark.masterservice.service.serviceImpl.NominationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/nomination")
@RequiredArgsConstructor
@Tag(name = "Nomination API", description = "APIs to manage nominations")
public class NominationController {

    private final NominationServiceImpl nominationService;

    /**
     * Endpoint to create a new nomination.
     *
     * @param requestDTO The request data for creating the nomination.
     * @return ResponseEntity containing the NominationResponseDTO.
     */
    @Operation(
            summary = "Create a new nomination",
            description = "Creates a nomination based on the provided details",
            responses = {
                @ApiResponse(responseCode = "200", description = "Nomination created successfully"),
            }
    )
    @PostMapping
    public ResponseEntity<BaseRes<NominationResponseDTO>> createNomination(
            @RequestBody NominationRequestDTO requestDTO) {
        log.info("Request received to create a nomination: {}", requestDTO);
        NominationResponseDTO responseDTO = nominationService.createNomination(requestDTO);
        log.info("Nomination created successfully with ID: {}", responseDTO.getId());
        return BaseRes.success(responseDTO,"", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get a nomination by ID",
            description = "Retrieve a nomination using its unique ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "Nomination retrieved successfully"),
                @ApiResponse(responseCode = "404", description = "Nomination not found")
            }
    )
    @GetMapping("/{id}")  // GET request to fetch a nomination by ID
    public ResponseEntity<BaseRes<NominationResponseDTO>> getNomination(@PathVariable Long id) {
        log.info("Request received to fetch nomination with ID: {}", id);  // Log incoming request
        NominationResponseDTO nomination = nominationService.getNominationById(id);  // Call service to retrieve the nomination
        log.info("Nomination retrieved successfully: {}", nomination);  // Log success
        return BaseRes.success(nomination, "", HttpStatus.OK);  // Return the retrieved nomination
    }

    /**
     * Endpoint to retrieve all nominations with pagination support.
     * 
     * @param page The page number for pagination (default is 0).
     * @param size The size of the page (default is 10).
     * @return ResponseEntity containing a paginated list of NominationResponseDTO.
     */
    @Operation(
            summary = "Get all nominations",
            description = "Retrieve a list of all nominations with pagination",
            responses = {
                @ApiResponse(responseCode = "200", description = "List of nominations retrieved successfully")
            }
    )
    @GetMapping
    public ResponseEntity<BaseRes<PaginatedRes<NominationResponseDTO>>> getAllNominations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Request received to fetch nominations with page: {} and size: {}", page, size);  // Log incoming request
        // Call service to get paginated nominations
        PaginatedRes<NominationResponseDTO> nominationsPage = nominationService.getAllNominations(page, size);
        log.info("Number of nominations retrieved: {}", nominationsPage.getData().size());  // Log the number of elements
        return BaseRes.success(nominationsPage, "", HttpStatus.OK);  // Return the paginated list of nominations
    }

    /**
     * Endpoint to update an existing nomination.
     * 
     * @param nominationId The ID of the nomination to update.
     * @param requestDTO The updated nomination details.
     * @return ResponseEntity containing the updated NominationResponseDTO.
     */
    @Operation(
            summary = "Update a nomination",
            description = "Update the details of an existing nomination using its ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "Nomination updated successfully"),
                @ApiResponse(responseCode = "404", description = "Nomination not found")
            }
    )
    @PutMapping("/{nominationId}")
    public ResponseEntity<BaseRes<NominationResponseDTO>> updateNomination(
            @PathVariable Long nominationId,
            @RequestBody NominationRequestDTO requestDTO) {
        log.info("Request received to update nomination with ID: {}", nominationId);
        NominationResponseDTO updatedNomination = nominationService.updateNomination(nominationId, requestDTO);  // Call service to update the nomination
        log.info("Nomination updated successfully: {}", updatedNomination);
        return BaseRes.success(updatedNomination, "", HttpStatus.OK);
    }
}
