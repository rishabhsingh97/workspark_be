package com.workspark.certificateservice.service;

import com.workspark.certificateservice.model.dto.request.CertificateReq;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for managing certificate templates and generating certificates.
 * Handles CRUD operations for templates and integrates with JasperReports for PDF generation.
 */
@Service
public interface CertificateService {

    /**
     * Generates a certificate based on the provided template and dynamic field data.
     *
     * @param certificateTemplateDto the certificate template data
     * @return the generated certificate as a byte array
     */
    byte[] generateCertificate(CertificateReq certificateTemplateDto);
    
    /**
     * Generates multiple certificates in parallel (batch operation).
     * 
     * Concepts demonstrated:
     * - CompletableFuture.allOf() for parallel execution
     * - Parallel certificate generation
     * - Batch processing with concurrency
     * 
     * @param certificateRequests List of certificate requests
     * @return CompletableFuture that completes with list of generated certificates
     */
    CompletableFuture<List<byte[]>> generateCertificatesBatch(List<CertificateReq> certificateRequests);
}
