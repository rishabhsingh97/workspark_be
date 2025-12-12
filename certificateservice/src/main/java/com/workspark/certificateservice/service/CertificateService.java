package com.workspark.certificateservice.service;

import com.workspark.certificateservice.model.dto.request.CertificateReq;
import org.springframework.stereotype.Service;

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
}
