package com.workspark.certificateservice.controller.impl;

import com.workspark.certificateservice.controller.CertificateController;
import com.workspark.certificateservice.exceptions.customExceptions.JasperException;
import com.workspark.certificateservice.model.dto.request.CertificateReq;
import com.workspark.certificateservice.service.CertificateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Certificate Generation", description = "API to generate certificates based on templates")
public class CertificateControllerImpl implements CertificateController {

    private final CertificateService certificateService;

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
}
