package com.workspark.certificateservice.controller.impl;

import com.workspark.certificateservice.exceptions.customExceptions.TemplateException;
import com.workspark.certificateservice.model.dto.request.TemplateReq;
import com.workspark.certificateservice.model.dto.response.TemplateRes;
import com.workspark.certificateservice.service.TemplateService;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.BasePageRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateControllerImplTest {

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private TemplateControllerImpl templateController;

    @BeforeEach
    void setUp() {
        // Initialize the mocks before each test
    }

    // Test for the uploadTemplate method
    @Test
    void uploadTemplate_shouldReturnTemplateRes_whenTemplateIsUploaded() {
        // Given
        TemplateReq templateReq = new TemplateReq();
        templateReq.setName("Test Template");
        templateReq.setDynamicFields(List.of("name"));
        templateReq.setTemplateCode("001");

        Map<String, MultipartFile> assets = new HashMap<>();

        // Mock the service layer
        TemplateRes templateRes = new TemplateRes();
        templateRes.setId(1L);
        when(templateService.uploadTemplate(any(TemplateReq.class), anyMap())).thenReturn(templateRes);

        // When
        ResponseEntity<BaseRes<TemplateRes>> responseEntity = templateController.uploadTemplate(templateReq, assets);
        BaseRes<TemplateRes> response = responseEntity.getBody();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
        assertEquals(1L, response.getItem().getId());
        verify(templateService, times(1)).uploadTemplate(any(TemplateReq.class), anyMap());
    }

    // Test for getAllTemplates method
    @Test
    void getAllTemplates_shouldReturnPaginatedTemplateRes_whenValidPageAndSize() {
        // Given
        BasePageRes<TemplateRes> paginatedRes = new BasePageRes<>();
        paginatedRes.setPageSize(5);

        paginatedRes.setCount(1);

        // Mock the service layer
        when(templateService.getAllTemplates(anyInt(), anyInt())).thenReturn(paginatedRes);

        // When
        ResponseEntity<BaseRes<BasePageRes<TemplateRes>>> responseEntity = templateController.getAllTemplates(1, 10);
        BaseRes<BasePageRes<TemplateRes>> response = responseEntity.getBody();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
        assertEquals(5, response.getItem().getPageSize());
        verify(templateService, times(1)).getAllTemplates(anyInt(), anyInt());
    }

    // Test for getTemplatesById method
    @Test
    void getTemplatesById_shouldReturnTemplateRes_whenTemplateIdExists() {
        // Given
        TemplateRes templateRes = new TemplateRes();
        templateRes.setId(1L);

        // Mock the service layer
        when(templateService.getTemplateById(anyLong())).thenReturn(templateRes);

        // When
        ResponseEntity<BaseRes<TemplateRes>> responseEntity = templateController.getTemplatesById(1L);
        BaseRes<TemplateRes> response = responseEntity.getBody();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
        assertEquals(1L, response.getItem().getId());
        verify(templateService, times(1)).getTemplateById(anyLong());
    }

    // Test for downloadById method (Preview Generation)
    @Test
    void downloadById_shouldReturnPdf_whenPreviewIsGenerated() {
        // Given
        byte[] pdf = new byte[]{1, 2, 3}; // Example byte array

        // Mock the service layer
        when(templateService.downloadTemplateById(anyLong())).thenReturn(pdf);

        // When
        ResponseEntity<byte[]> responseEntity = templateController.downloadById(1L);

        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCode().value());
        assertArrayEquals(pdf, responseEntity.getBody());
        verify(templateService, times(1)).downloadTemplateById(anyLong());
    }

    // Test for downloadById method (Exception Case)
    @Test
    void downloadById_shouldThrowException_whenServiceThrowsError() {
        // Given
        when(templateService.downloadTemplateById(anyLong())).thenThrow(new TemplateException("Error generating preview"));

        // When & Then
        assertThrows(TemplateException.class, () -> templateController.downloadById(1L));
    }
}
