package com.workspark.certificateservice.controller.impl;

import com.workspark.certificateservice.exceptions.customExceptions.JasperException;
import com.workspark.certificateservice.model.dto.request.CertificateReq;
import com.workspark.certificateservice.service.CertificateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateControllerImplTest {

    @Mock
    private CertificateService certificateService;

    @InjectMocks
    private CertificateControllerImpl certificateController;

    private CertificateReq certificateReq;
    private byte[] samplePdfData;

    @BeforeEach
    void setUp() {
        certificateReq = CertificateReq.builder()
                .templateId(1L)
                .build();
        samplePdfData = "sample pdf content".getBytes();
    }

    @Test
    void generateCertificate_PDF_Success() {
        // Arrange
        certificateReq.setType("pdf");
        when(certificateService.generateCertificate(certificateReq)).thenReturn(samplePdfData);

        // Act
        ResponseEntity<byte[]> response = certificateController.generateCertificate(certificateReq);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals("attachment; filename=certificate.pdf",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertArrayEquals(samplePdfData, response.getBody());

        verify(certificateService, times(1)).generateCertificate(certificateReq);
    }

    @Test
    void generateCertificate_JPG_Success() {
        // Arrange
        certificateReq.setType("jpg");
        when(certificateService.generateCertificate(certificateReq)).thenReturn(samplePdfData);

        // Act
        ResponseEntity<byte[]> response = certificateController.generateCertificate(certificateReq);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        assertEquals("attachment; filename=certificate.jpg",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertArrayEquals(samplePdfData, response.getBody());
    }

    @Test
    void generateCertificate_PNG_Success() {
        // Arrange
        certificateReq.setType("png");
        when(certificateService.generateCertificate(certificateReq)).thenReturn(samplePdfData);

        // Act
        ResponseEntity<byte[]> response = certificateController.generateCertificate(certificateReq);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());
        assertEquals("attachment; filename=certificate.png",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertArrayEquals(samplePdfData, response.getBody());
    }

    @Test
    void generateCertificate_InvalidType_ThrowsJasperException() {
        // Arrange
        certificateReq.setType("invalid");
        when(certificateService.generateCertificate(certificateReq)).thenReturn(samplePdfData);

        // Act & Assert
        JasperException exception = assertThrows(JasperException.class, () ->
                certificateController.generateCertificate(certificateReq));

        assertEquals("Invalid certificate type", exception.getMessage());
    }

    @Test
    void generateCertificate_ServiceException_ThrowsException() {
        // Arrange
        certificateReq.setType("pdf");
        RuntimeException serviceException = new RuntimeException("Service error");
        when(certificateService.generateCertificate(certificateReq)).thenThrow(serviceException);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                certificateController.generateCertificate(certificateReq));

        assertEquals("Service error", exception.getMessage());
        verify(certificateService, times(1)).generateCertificate(certificateReq);
    }
}
