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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

/**
 * Service class for managing certificate templates and generating certificates.
 * Handles CRUD operations for templates and integrates with JasperReports for PDF generation.
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
}
