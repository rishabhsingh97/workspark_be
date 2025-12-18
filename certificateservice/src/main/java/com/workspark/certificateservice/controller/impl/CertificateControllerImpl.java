package com.workspark.certificateservice.controller.impl;

import com.workspark.certificateservice.controller.CertificateController;
import com.workspark.certificateservice.exceptions.customExceptions.JasperException;
import com.workspark.certificateservice.model.dto.request.CertificateReq;
import com.workspark.certificateservice.model.dto.response.CertificateGenerationResponse;
import com.workspark.certificateservice.model.dto.response.CertificateJobStatus;
import com.workspark.certificateservice.service.CertificateJobService;
import com.workspark.certificateservice.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Certificate Generation", description = "API to generate certificates based on templates")
public class CertificateControllerImpl implements CertificateController {

    private final CertificateService certificateService;
    private final CertificateJobService certificateJobService;

    /**
     * Endpoint to generate a certificate based on a template.
     *
     * @param certificateReq DTO for generating the certificate.
     * @return The generated certificate as a PDF, JPG, or PNG file.
     */
    @PostMapping
    public ResponseEntity<byte[]> generateCertificate(CertificateReq certificateReq) {
        log.info("Received request to generate certificate using template ID: {}", certificateReq.getTemplateId());
        log.debug("Request details: {}", certificateReq);

        log.info("Calling service to generate certificate...");
        byte[] pdf = certificateService.generateCertificate(certificateReq);
        log.info("Certificate generated successfully for template ID: {}", certificateReq.getTemplateId());

        MediaType contentType;
        String filename = switch (certificateReq.getType()) {
            case "pdf" -> {
                log.debug("Setting content type to PDF.");
                contentType = MediaType.APPLICATION_PDF;
                yield "certificate.pdf";
            }
            case "jpg" -> {
                log.debug("Setting content type to JPG.");
                contentType = MediaType.IMAGE_JPEG;
                yield "certificate.jpg";
            }
            case "png" -> {
                log.debug("Setting content type to PNG.");
                contentType = MediaType.IMAGE_PNG;
                yield "certificate.png";
            }
            default -> {
                log.error("Invalid certificate type provided: {}", certificateReq.getType());
                throw new JasperException("Invalid certificate type");
            }
        };

        log.info("Preparing response with filename: {}", filename);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=".concat(filename))
                .contentType(contentType)
                .body(pdf);
    }
    
    /**
     * Async endpoint to generate certificate.
     * Returns immediately with a job ID, certificate is generated in background.
     * 
     * Concepts demonstrated:
     * - Non-blocking endpoint
     * - Async processing
     * - Job tracking
     * 
     * @param certificateReq Certificate request
     * @return Job ID and status
     */
    @PostMapping("/async")
    @Operation(summary = "Generate certificate asynchronously", 
               description = "Submits a certificate generation job and returns immediately with a job ID")
    public ResponseEntity<CertificateGenerationResponse> generateCertificateAsync(
            @RequestBody CertificateReq certificateReq) {
        log.info("Received async certificate generation request for template ID: {}", 
                certificateReq.getTemplateId());
        
        CompletableFuture<String> jobFuture = certificateJobService.submitCertificateGenerationJob(
                certificateReq.getTemplateId(),
                certificateReq.getDynamicFieldData(),
                certificateReq.getType()
        );
        
        // Get job ID immediately (returns immediately, generation happens in background)
        String jobId = jobFuture.join();
        
        CertificateGenerationResponse response = CertificateGenerationResponse.builder()
                .jobId(jobId)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .message("Certificate generation job submitted successfully. Use job ID to check status.")
                .build();
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
    
    /**
     * Get the status of a certificate generation job.
     * 
     * @param jobId Job ID
     * @return Job status
     */
    @GetMapping("/status/{jobId}")
    @Operation(summary = "Get certificate generation job status",
               description = "Check the status of an async certificate generation job")
    public ResponseEntity<CertificateJobStatus> getJobStatus(@PathVariable String jobId) {
        log.info("Checking status for job ID: {}", jobId);
        CertificateJobStatus status = certificateJobService.getJobStatus(jobId);
        return ResponseEntity.ok(status);
    }
    
    /**
     * Download the generated certificate when job is completed.
     * 
     * @param jobId Job ID
     * @return Certificate file
     */
    @GetMapping("/download/{jobId}")
    @Operation(summary = "Download generated certificate",
               description = "Download the certificate once generation is completed")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable String jobId) {
        log.info("Downloading certificate for job ID: {}", jobId);
        byte[] certificateData = certificateJobService.getCertificateData(jobId);
        
        MediaType contentType = MediaType.APPLICATION_PDF;
        String filename = "certificate_" + jobId + ".pdf";
        
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=".concat(filename))
                .contentType(contentType)
                .body(certificateData);
    }
    
    /**
     * Batch endpoint to generate multiple certificates in parallel.
     * 
     * Concepts demonstrated:
     * - Parallel batch processing
     * - CompletableFuture for async operations
     * 
     * @param certificateRequests List of certificate requests
     * @return CompletableFuture with list of certificates
     */
    @PostMapping("/batch")
    @Operation(summary = "Generate multiple certificates in parallel",
               description = "Generates multiple certificates concurrently for better performance")
    public CompletableFuture<ResponseEntity<List<byte[]>>> generateCertificatesBatch(
            @RequestBody List<CertificateReq> certificateRequests) {
        log.info("Received batch certificate generation request for {} certificates", 
                certificateRequests.size());
        
        CompletableFuture<List<byte[]>> certificatesFuture = 
                certificateService.generateCertificatesBatch(certificateRequests);
        
        return certificatesFuture.thenApply(certificates -> {
            log.info("Batch certificate generation completed. Generated {} certificates", 
                    certificates.size());
            return ResponseEntity.ok(certificates);
        }).exceptionally(throwable -> {
            log.error("Error in batch certificate generation", throwable);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        });
    }
}
