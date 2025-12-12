package com.workspark.nominationservice.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import com.workspark.nominationservice.dto.RecognitionCategoryRequestDTO;
import com.workspark.nominationservice.dto.RecognitionCategoryResponseDTO;
import com.workspark.nominationservice.service.RecognitionCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle Recognition Category related operations.
 *
 * @author sriyas
 */

@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "Recognition Category", description = "APIs for managing recognition categories")
@Slf4j
public class RecognitionCategoryController {

    private final RecognitionCategoryService recognitionCategoryService;

    /**
     * Constructor for RecognitionCategoryController.
     *
     * @param recognitionCategoryService the service handling category operations
     */
    public RecognitionCategoryController(RecognitionCategoryService recognitionCategoryService) {
        this.recognitionCategoryService = recognitionCategoryService;
    }

    /**
     * Endpoint to create a new recognition category.
     *
     * @param dto the data transfer object containing category details
     * @return the created category wrapped in a ResponseEntity
     */
    @PostMapping("")
    @Operation(summary = "Create a new recognition category", description = "Adds a new recognition category with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid category data")
    })
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<BaseRes<RecognitionCategoryResponseDTO>> createRecognitionCategory(@RequestBody RecognitionCategoryRequestDTO dto) throws JsonMappingException {
        log.info("Creating a new recognition category with name: {}", dto.getCategoryName());
        RecognitionCategoryResponseDTO response = recognitionCategoryService.createOrUpdateRecognitionCategory(dto);
        log.info("Recognition category created successfully with ID: {}", response.getCategoryId());
        return BaseRes.success(response, "", HttpStatus.CREATED);
    }

    /**
     * Endpoint to update an existing recognition category.
     *
     * @param dto the data transfer object containing updated details
     * @return the updated category wrapped in a ResponseEntity
     */
    @PutMapping("")
    @Operation(summary = "Update an existing recognition category", description = "Updates the recognition category withdetails.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<BaseRes<RecognitionCategoryResponseDTO>> updateCategory(@RequestBody RecognitionCategoryRequestDTO dto) {
        log.info("Updating recognition category with ID: {}", dto.getCategoryId());
        // Ensure the ID is set in the DTO
        RecognitionCategoryResponseDTO response = recognitionCategoryService.createOrUpdateRecognitionCategory(dto);
        log.info("Recognition category updated successfully with ID: {}", response.getCategoryId());
        return BaseRes.success(response, "", HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve a recognition category by ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the category details wrapped in a ResponseEntity
     */
    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a recognition category by ID", description = "Fetches the details of a recognition category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<BaseRes<RecognitionCategoryResponseDTO>> getRecognitionCategory(@PathVariable Long categoryId) {
        log.info("Fetching recognition category with ID: {}", categoryId);
        RecognitionCategoryResponseDTO response = recognitionCategoryService.getRecognitionCategory(categoryId);
        log.info("Recognition category fetched successfully with ID: {}", categoryId);
        return BaseRes.success(response, "", HttpStatus.OK);
    }


    @GetMapping("/all")
    @Operation(summary = "Get all recognition categories", description = "Fetches all recognition categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All recognition categories fetched successfully")
    })
    @PreAuthorize("hasRole('TENANT_ADMIN')")
    public ResponseEntity<BaseRes<PaginatedRes<RecognitionCategoryResponseDTO>>> getAllRecognitionCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Fetching all recognition categories");
        PaginatedRes<RecognitionCategoryResponseDTO> response = recognitionCategoryService.getAllRecognitionCategories(page, size);
        log.info("All recognition categories fetched successfully");
        return BaseRes.success(response, "", HttpStatus.OK);
    }

    @GetMapping("/auth-user")
    @Operation(summary = "Get all recognition categories available to auth user", description = "Fetches all recognition categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All recognition categories fetched successfully")
    })
    public ResponseEntity<BaseRes<PaginatedRes<RecognitionCategoryResponseDTO>>> getAllRecognitionCategoriesForAuthUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Fetching all recognition categories by auth user");
        PaginatedRes<RecognitionCategoryResponseDTO> response = recognitionCategoryService.getAllRecognitionCategoriesByAuthUser(page, size);
        log.info("All recognition categories  by auth user fetched successfully");
        return BaseRes.success(response, "", HttpStatus.OK);
    }
}
