package com.workspark.certificateservice.service.impl;

import com.workspark.certificateservice.exceptions.customExceptions.CertificateException;
import com.workspark.certificateservice.exceptions.customExceptions.JasperException;
import com.workspark.certificateservice.exceptions.customExceptions.TemplateException;
import com.workspark.certificateservice.model.dto.request.CertificateReq;
import com.workspark.certificateservice.model.entity.Template;
import com.workspark.certificateservice.model.entity.TemplateDynamicField;
import com.workspark.certificateservice.repository.TemplateRepository;
import com.workspark.certificateservice.service.CertificateService;
import com.workspark.certificateservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service class for managing certificate templates and generating certificates.
 * Handles CRUD operations for templates and integrates with JasperReports for PDF generation.
 * 
 * Concepts demonstrated:
 * - Async certificate generation
 * - Parallel batch processing
 * - CompletableFuture for concurrent operations
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CertificateServiceImpl implements CertificateService {

    private final TemplateRepository templateRepository;
    private final ReportService reportService;

    @Override
    public byte[] generateCertificate(CertificateReq certificateReq) {
        log.info("Generating certificate using template ID: {}", certificateReq.getTemplateId());
        log.debug("Request details: {}", certificateReq);

        Optional<Template> templateOpt = templateRepository.findById(certificateReq.getTemplateId());
        if (templateOpt.isEmpty()) {
            log.error("Template with ID {} not found", certificateReq.getTemplateId());
            throw new TemplateException("Template not found", HttpStatus.NOT_FOUND);
        }

        Template template = templateOpt.get();
        log.info("Template with ID {} found. Validating dynamic fields...", certificateReq.getTemplateId());
        validateDynamicFields(template, certificateReq);
        log.info("Dynamic fields validated successfully for template ID: {}", certificateReq.getTemplateId());

        return generateCertificateData(certificateReq, template);
    }

    /**
     * Helper method to generate certificate data (PDF or image).
     *
     * @param certificateReq the request details for certificate generation
     * @param template       the template to use for generation
     * @return the generated certificate as a byte array
     */
    private byte[] generateCertificateData(CertificateReq certificateReq, Template template) {
        log.info("Generating certificate of type '{}' for template ID: {}", certificateReq.getType(), certificateReq.getTemplateId());
        try {
            byte[] data;
            switch (certificateReq.getType()) {
                case "pdf":
                    log.info("Generating PDF certificate...");
                    data = reportService.generateCertificatePdf(
                            getFileData(template.getFileData()),
                            certificateReq.getDynamicFieldData(),
                            template.getAssets()
                    );
                    log.info("PDF certificate generated successfully for template ID: {}", certificateReq.getTemplateId());
                    break;
                case "jpg", "png":
                    log.info("Generating image certificate...");
                    data = reportService.generateCertificateImage(
                            getFileData(template.getFileData()),
                            certificateReq.getDynamicFieldData(),
                            template.getAssets(),
                            certificateReq.getType()
                    );
                    log.info("Image certificate generated successfully for template ID: {}", certificateReq.getTemplateId());
                    break;
                default:
                    log.error("Invalid certificate type '{}' provided for template ID: {}", certificateReq.getType(), certificateReq.getTemplateId());
                    throw new CertificateException("Invalid certificate type", HttpStatus.CONFLICT);
            }
            return data;
        } catch (Exception e) {
            log.error("Error generating certificate for template ID: {}: {}", certificateReq.getTemplateId(), e.getMessage(), e);
            throw new CertificateException("Error generating certificate: ".concat(e.getMessage()));
        }
    }

    /**
     * Helper method to safely retrieve byte data from a Blob.
     */
    private byte[] getFileData(Blob blob) {
        log.info("Retrieving file data from blob...");
        try (InputStream inputStream = blob.getBinaryStream()) {
            byte[] data = inputStream.readAllBytes();
            log.info("File data retrieved successfully from blob.");
            return data;
        } catch (SQLException | IOException e) {
            log.error("Error reading file data from blob", e);
            throw new JasperException("Error reading file data from blob");
        }
    }

    private void validateDynamicFields(Template template, CertificateReq certificateReq) {
        log.info("Validating dynamic fields for template ID: {}", template.getId());
        List<String> templateDynamicFields = Optional.ofNullable(template.getDynamicFields())
                .orElse(Collections.emptyList())
                .stream()
                .map(TemplateDynamicField::getDynamicField)
                .toList();
        Set<String> dtoDynamicFieldKeys = certificateReq.getDynamicFieldData().keySet();

        log.debug("Template dynamic fields: {}", templateDynamicFields);
        log.debug("Request dynamic fields: {}", dtoDynamicFieldKeys);

        if (!new HashSet<>(templateDynamicFields).equals(dtoDynamicFieldKeys)) {
            log.error("Dynamic fields validation failed for template ID: {}", template.getId());
            throw new IllegalArgumentException("Dynamic fields do not match with the template parameters");
        }
    }
    
    /**
     * Generates multiple certificates in parallel (batch operation).
     * 
     * Concepts demonstrated:
     * - CompletableFuture.allOf(): Waits for all async tasks to complete
     * - CompletableFuture.supplyAsync(): Creates async tasks for each certificate
     * - Parallel execution: All certificates are generated concurrently
     * - Exception handling: Failed certificates are handled gracefully
     * - Stream API: Used for collecting results
     * 
     * Performance benefits:
     * - If generating 10 certificates sequentially takes 10 seconds (1 sec each)
     * - Parallel generation with 10 threads takes ~1 second (all run concurrently)
     * 
     * @param certificateRequests List of certificate requests
     * @return CompletableFuture that completes with list of generated certificates
     */
    @Override
    @Async("batchCertificateExecutor")
    public CompletableFuture<List<byte[]>> generateCertificatesBatch(List<CertificateReq> certificateRequests) {
        log.info("Starting batch certificate generation for {} certificates", certificateRequests.size());
        
        // Create a list of CompletableFutures, one for each certificate
        // Each certificate generation runs asynchronously
        List<CompletableFuture<byte[]>> futures = certificateRequests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    try {
                        log.debug("Generating certificate for template ID: {}", request.getTemplateId());
                        return generateCertificate(request);
                    } catch (Exception e) {
                        log.error("Error generating certificate for template ID: {}", request.getTemplateId(), e);
                        // Return null or throw exception based on requirements
                        // For now, we'll throw to fail fast
                        throw new RuntimeException("Failed to generate certificate: " + e.getMessage(), e);
                    }
                }))
                .collect(Collectors.toList());
        
        // Wait for all futures to complete
        // CompletableFuture.allOf() returns a CompletableFuture<Void> that completes
        // when all the provided futures complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        
        // Once all futures complete, extract the results
        CompletableFuture<List<byte[]>> resultFuture = allFutures.thenApply(v -> {
            return futures.stream()
                    .map(CompletableFuture::join) // Get the result (blocks until complete, but we know it's done)
                    .collect(Collectors.toList());
        });
        
        // Handle exceptions
        resultFuture.exceptionally(throwable -> {
            log.error("Error in batch certificate generation", throwable);
            throw new RuntimeException("Batch certificate generation failed", throwable);
        });
        
        log.info("Batch certificate generation initiated for {} certificates", certificateRequests.size());
        return resultFuture;
    }
}
