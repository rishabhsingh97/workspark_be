package com.workspark.certificateservice.controller;

import com.workspark.certificateservice.model.dto.request.TemplateReq;
import com.workspark.certificateservice.model.dto.response.TemplateRes;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.BasePageRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controller for managing certificate templates and generating certificates.
 * Provides endpoints for uploading templates, retrieving templates, and generating certificates.
 */
@RequestMapping("/api/v1/template")
public interface TemplateController {

    /**
     * Endpoint to upload a new certificate template.
     *
     * @param templateReq the template request containing template details and dynamic fields
     * @param assets      the assets (files) associated with the template
     * @return the uploaded template details
     */
    @Operation(
            summary = "Upload a new certificate template",
            description = "Uploads a new template with its assets and dynamic fields for certificate generation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Template uploaded successfully",
                            content = @Content(schema = @Schema(implementation = BaseRes.class, subTypes = TemplateRes.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input or missing template file",
                            content = @Content(schema = @Schema(implementation = BaseRes.class, subTypes = TemplateRes.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = BaseRes.class, subTypes = TemplateRes.class)))
            }
    )
    @PostMapping
    ResponseEntity<BaseRes<TemplateRes>> uploadTemplate(
            @Valid TemplateReq templateReq,
            @RequestParam Map<String, MultipartFile> assets
    );

    /**
     * Endpoint to retrieve all templates.
     * This method is paginated and returns a list of TemplateDto objects.
     *
     * @param page the page number to retrieve
     * @param size the number of templates per page
     * @return a list of all templates
     */
    @Operation(
            summary = "Get all templates",
            description = "Fetches a paginated list of all uploaded templates",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Templates fetched successfully",
                            content = @Content(schema = @Schema(contentSchema = BaseRes.class, subTypes = {BasePageRes.class, TemplateRes.class}))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(contentSchema = BaseRes.class, subTypes = {BasePageRes.class, TemplateRes.class})))
            }
    )
    @GetMapping
    ResponseEntity<BaseRes<BasePageRes<TemplateRes>>> getAllTemplates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size
    );


    /**
     * Endpoint to retrieve all templates.
     * This method is paginated and returns a list of TemplateDto objects.
     *
     * @return a list of all templates
     */
    @Operation(
            summary = "Get all templates",
            description = "Fetches a paginated list of all uploaded templates",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Templates fetched successfully",
                            content = @Content(schema = @Schema(contentSchema = BaseRes.class, subTypes = {BasePageRes.class, TemplateRes.class}))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(contentSchema = BaseRes.class, subTypes = {BasePageRes.class, TemplateRes.class})))
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<BaseRes<TemplateRes>> getTemplatesById(
            @PathVariable Long id
    );

    /**
     * Endpoint to generate a certificate preview for a given template ID.
     *
     * @param id the ID of the template
     * @return the generated certificate preview
     */
    @Operation(
            summary = "Preview a certificate template by ID",
            description = "Generates a preview for the template as an image",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Certificate preview generated successfully",
                            content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Template not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseRes.class, subTypes = TemplateRes.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BaseRes.class, subTypes = TemplateRes.class))),
            }
    )
    @GetMapping("/preview/{id}")
    ResponseEntity<byte[]> downloadById(@PathVariable Long id);

}
