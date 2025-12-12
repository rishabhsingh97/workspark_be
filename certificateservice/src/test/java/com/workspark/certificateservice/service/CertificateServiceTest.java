package com.workspark.certificateservice.service;

import com.workspark.certificateservice.exceptions.customExceptions.CertificateException;
import com.workspark.certificateservice.exceptions.customExceptions.TemplateException;
import com.workspark.certificateservice.model.dto.request.CertificateReq;
import com.workspark.certificateservice.model.entity.Template;
import com.workspark.certificateservice.model.entity.TemplateAsset;
import com.workspark.certificateservice.model.entity.TemplateDynamicField;
import com.workspark.certificateservice.repository.TemplateRepository;
import com.workspark.certificateservice.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    @Test
    void generateCertificate_PdfType_Success() throws Exception {

        // Arrange: Prepare the request, template, and mock dependencies

        // Sample Certificate Request
        CertificateReq request = new CertificateReq();
        request.setTemplateId(1L);  // ID of the template to fetch
        request.setType("pdf");     // Type is pdf
        request.setDynamicFieldData(Map.of("name", "John Doe"));

        // Template preparation: mock the template and its assets

        // Mock compiled report byte data (simulating the result of compiling the JRXML file)
        byte[] compiledReport = new byte[]{1, 2, 3, 4}; // Example byte array, replace with actual compiled report byte data

        // Mock dynamic fields for template
        TemplateDynamicField templateDynamicField = TemplateDynamicField.builder()
                .dynamicField("name")
                .build();

        // Mock assets (e.g., background image)
        TemplateAsset templateAsset = TemplateAsset.builder()
                .name("bg_image")
                .assetData(mock(Blob.class))  // Mock the blob for asset data
                .build();

        Template template = new Template();
        template.setId(1L);
        template.setTemplateCode("001");
        template.setName("Certificate Template");
        template.setFileData(new SerialBlob(compiledReport));
        template.setPreviewFile(new SerialBlob(compiledReport));
        template.setAssets(List.of(templateAsset));
        template.setDynamicFields(List.of(templateDynamicField));

        // Mock repository to return the template when queried by ID
        when(templateRepository.findById(template.getId())).thenReturn(Optional.of(template));

        // Mock reportService's PDF generation logic
        when(reportService.generateCertificatePdf(any(byte[].class), anyMap(), anyList())).thenReturn(new byte[20]);

        // Act: Call the method to generate certificate
        byte[] result = certificateService.generateCertificate(request);

        // Assert: Verify the result and interactions
        assertNotNull(result);
        assertEquals(20, result.length);  // Assert that the generated PDF byte array has the expected length

        // Verify interactions with repository and reportService
        verify(templateRepository, times(1)).findById(1L);  // Ensure templateRepository.findById was called once
        verify(reportService, times(1)).generateCertificatePdf(any(byte[].class), anyMap(), anyList());  // Verify PDF generation
    }

    @Test
    void generateCertificate_ImageType_Success() throws Exception {

        // Arrange: Prepare the request, template, and mock dependencies

        // Sample Certificate Request
        CertificateReq request = new CertificateReq();
        request.setTemplateId(1L);  // ID of the template to fetch
        request.setType("jpg");     // Type is jpg
        request.setDynamicFieldData(Map.of("name", "John Doe"));

        // Template preparation: mock the template and its assets

        // Mock compiled report byte data (simulating the result of compiling the JRXML file)
        byte[] compiledReport = new byte[]{1, 2, 3, 4}; // Example byte array, replace with actual compiled report byte data

        // Mock dynamic fields for template
        TemplateDynamicField templateDynamicField = TemplateDynamicField.builder()
                .dynamicField("name")
                .build();

        // Mock assets (e.g., background image)
        TemplateAsset templateAsset = TemplateAsset.builder()
                .name("bg_image")
                .assetData(mock(Blob.class))  // Mock the blob for asset data
                .build();

        Template template = new Template();
        template.setId(1L);
        template.setTemplateCode("001");
        template.setName("Certificate Template");
        template.setFileData(new SerialBlob(compiledReport));
        template.setPreviewFile(new SerialBlob(compiledReport));
        template.setAssets(List.of(templateAsset));
        template.setDynamicFields(List.of(templateDynamicField));

        // Mock repository to return the template when queried by ID
        when(templateRepository.findById(template.getId())).thenReturn(Optional.of(template));

        // Mock reportService's PDF generation logic
        when(reportService.generateCertificateImage(any(byte[].class), anyMap(), anyList(), anyString())).thenReturn(new byte[20]);

        // Act: Call the method to generate certificate
        byte[] result = certificateService.generateCertificate(request);

        // Assert: Verify the result and interactions
        assertNotNull(result);
        assertEquals(20, result.length);  // Assert that the generated PDF byte array has the expected length

        // Verify interactions with repository and reportService
        verify(templateRepository, times(1)).findById(1L);  // Ensure templateRepository.findById was called once
        verify(reportService, times(1)).generateCertificateImage(any(byte[].class), anyMap(), anyList(), anyString());  // Verify PDF generation
    }

    @Test
    void generateCertificate_InvalidType_ThrowsException() {
        // Arrange
        CertificateReq request = new CertificateReq();
        request.setTemplateId(1L);
        request.setType("invalid");

        when(templateRepository.findById(1L)).thenReturn(Optional.of(new Template()));

        // Act & Assert
        CertificateException exception = assertThrows(CertificateException.class, () -> certificateService.generateCertificate(request));
        assertEquals("Error generating certificate: Invalid certificate type", exception.getMessage());
    }

    @Test
    void generateCertificate_TemplateNotFound_ThrowsException() {
        // Arrange
        CertificateReq request = new CertificateReq();
        request.setTemplateId(99L);
        request.setType("pdf");

        when(templateRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TemplateException.class, () -> certificateService.generateCertificate(request));
        verify(templateRepository, times(1)).findById(99L);
    }


    @Test
    void generateCertificate_DynamicFieldsValidatoinFailed_ThrowsException() {
        // Arrange
        CertificateReq request = new CertificateReq();
        request.setTemplateId(1L);
        request.setType("invalid");
        request.setDynamicFieldData(Map.of("randomKey", "randomValue"));

        when(templateRepository.findById(1L)).thenReturn(Optional.of(new Template()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> certificateService.generateCertificate(request));
        assertEquals("Dynamic fields do not match with the template parameters", exception.getMessage());
    }

    @Test
    void generateCertificate_shouldThrowJasperException_whenBlobSQLExceptionOccurs() throws Exception {
        // Arrange
        CertificateReq request = new CertificateReq();
        request.setTemplateId(1L);
        request.setType("pdf");
        request.setDynamicFieldData(Map.of("name", "John Doe"));

        Blob mockBlob = mock(Blob.class);

        // Simulate SQLException when getBinaryStream is called
        when(mockBlob.getBinaryStream()).thenThrow(new SQLException("Blob reading failed"));

        // Prepare template with the mockBlob as file data
        Template template = new Template();
        template.setId(1L);
        template.setFileData(mockBlob);
        template.setDynamicFields(List.of(TemplateDynamicField.builder().dynamicField("name").build()));
        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));

        // Act & Assert
        CertificateException exception = assertThrows(CertificateException.class, () -> {
            certificateService.generateCertificate(request);
        });

        // Assert exception message
        assertTrue(exception.getMessage().contains("Error reading file data from blob"));
    }

}
