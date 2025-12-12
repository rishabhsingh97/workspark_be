package com.workspark.certificateservice.controller;

import com.workspark.certificateservice.model.dto.request.CertificateReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling certificate-related operations.
 */
@RequestMapping("/api/v1/cert")
public interface CertificateController {

    /**
     * Endpoint to generate a certificate based on a template.
     *
     * @param certificateReq dto for generating certificate
     * @return the generated certificate as a PDF
     */
    @Operation(
            summary = "Generate a certificate",
            description = "Generates a certificate in the specified format (PDF, JPG, or PNG) based on the provided template and dynamic data.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Certificate generation request with template ID, dynamic data, and certificate type (pdf, jpg, png)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CertificateReq.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Certificate generated successfully",
                            content = @Content(
                                    mediaType = "application/pdf",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid certificate type provided"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error during certificate generation"
                    )
            }
    )
    @PostMapping
    ResponseEntity<byte[]> generateCertificate(@RequestBody @Valid CertificateReq certificateReq);

}

