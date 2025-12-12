package com.workspark.certificateservice.service;

import com.workspark.certificateservice.model.entity.TemplateAsset;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Service interface for report-related operations.
 */
@Service
public interface ReportService {

    /**
     * Compiles a Jasper template file.
     *
     * @param file the MultipartFile containing the Jasper template
     * @return a byte array representing the compiled template
     */
    byte[] compileTemplateFile(MultipartFile file);

    /**
     * Generates a certificate PDF from a Jasper template and dynamic fields.
     * @param pdfSourceData     the byte array representing the Jasper template
     * @param dynamicFieldsData a map containing dynamic fields and their values
     * @param assets            a list of assets to be included in the certificate
     * @return  a byte array representing the generated certificate PDF
     */
    byte[] generateCertificatePdf(byte[] pdfSourceData, Map<String, Object> dynamicFieldsData, List<TemplateAsset> assets);

    /**
     * Generates a certificate image from a Jasper template and dynamic fields.
     * @param pdfSourceData     the byte array representing the Jasper template
     * @param dynamicFieldsData     a map containing dynamic fields and their values
     * @param assets            a list of assets to be included in the certificate
     * @param type              the image type (e.g., "jpg", "png")
     * @return  a byte array representing the generated certificate image
     */
    byte[] generateCertificateImage(byte[] pdfSourceData, Map<String, Object> dynamicFieldsData, List<TemplateAsset> assets, String type);
}
