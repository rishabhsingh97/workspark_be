package com.workspark.certificateservice.service;

import com.workspark.certificateservice.exceptions.customExceptions.TemplateException;
import com.workspark.certificateservice.model.dto.request.TemplateReq;
import com.workspark.certificateservice.model.dto.response.TemplateRes;
import com.workspark.certificateservice.model.entity.Template;
import com.workspark.certificateservice.repository.TemplateAssetRepository;
import com.workspark.certificateservice.repository.TemplateDynamicFieldRepository;
import com.workspark.certificateservice.repository.TemplateRepository;
import com.workspark.certificateservice.service.impl.TemplateServiceImpl;
import com.workspark.models.response.PaginatedRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @InjectMocks
    private TemplateServiceImpl templateService;

    @Mock
    private ReportService reportService;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private TemplateAssetRepository templateAssetRepository;

    @Mock
    private TemplateDynamicFieldRepository templateDynamicFieldRepository;

    @BeforeEach
    void setUp() {
        templateService = new TemplateServiceImpl(
                reportService,
                templateRepository,
                templateAssetRepository,
                templateDynamicFieldRepository
        );
    }

    @Test
    void testUploadTemplate() {

        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("template", "template.jrxml", "text/xml", "<xml></xml>".getBytes());
        byte[] compiledTemplate = new byte[]{1, 2, 3};
        byte[] previewImage = new byte[]{10, 20, 30};

        when(reportService.compileTemplateFile(mockFile)).thenReturn(compiledTemplate);
        when(reportService.generateCertificateImage(any(byte[].class), anyMap(), anyList(), eq("jpg"))).thenReturn(previewImage);
        when(templateRepository.save(any(Template.class))).thenAnswer(invocation -> {
            Template template = invocation.getArgument(0);
            template.setId(1L);
            return template;
        });

        String name = "Test Template";
        String templateCode = "TEST001";
        List<String> dynamicFields = Arrays.asList("field1", "field2");

        TemplateReq templateReq = TemplateReq.builder()
                .name(name)
                .templateCode(templateCode)
                .dynamicFields(dynamicFields)
                .build();

        // Act
        TemplateRes result = templateService.uploadTemplate(
                templateReq,
                Map.of("template", mockFile, "logo", mockFile)
        );

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Template", result.getName());
        verify(reportService).compileTemplateFile(mockFile);
        verify(reportService).generateCertificateImage(eq(compiledTemplate), anyMap(), anyList(), eq("jpg"));
        verify(templateRepository).save(any(Template.class));
    }

    @Test
    void testGetAllTemplates() throws Exception {
        // Arrange
        Blob mockPreviewBlob = new SerialBlob(new byte[]{10, 20, 30});
        Template mockTemplate = Template.builder()
                .id(1L)
                .name("Template 1")
                .templateCode("CODE1")
                .previewFile(mockPreviewBlob)
                .build();

        Page<Template> mockPage = new PageImpl<>(Collections.singletonList(mockTemplate));
        when(templateRepository.findAll(PageRequest.of(0, 10))).thenReturn(mockPage);

        // Act
        PaginatedRes<TemplateRes> result = templateService.getAllTemplates(1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals("Template 1", result.getData().getFirst().getName());
        assertEquals("CODE1", result.getData().getFirst().getTemplateCode());
        assertEquals(Base64.getEncoder().encodeToString(new byte[]{10, 20, 30}), result.getData().getFirst().getBase64Preview());
    }

    @Test
    void testDownloadTemplateById() throws Exception {
        // Arrange
        Blob mockPreviewBlob = new SerialBlob(new byte[]{10, 20, 30});
        Template mockTemplate = Template.builder()
                .id(1L)
                .name("Template 1")
                .previewFile(mockPreviewBlob)
                .build();

        when(templateRepository.findById(1L)).thenReturn(Optional.of(mockTemplate));

        // Act
        byte[] result = templateService.downloadTemplateById(1L);

        // Assert
        assertNotNull(result);
        assertArrayEquals(new byte[]{10, 20, 30}, result);
        verify(templateRepository).findById(1L);
    }

    @Test
    void testDownloadTemplateByIdThrowsException() {
        // Arrange
        when(templateRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> templateService.downloadTemplateById(1L));
        assertEquals("Template not found", exception.getMessage());
        verify(templateRepository).findById(1L);
    }

    @Test
    void testGetTemplateById_Success() throws SQLException {
        // Arrange
        Blob previewBlob = new SerialBlob(new byte[]{7, 8, 9});
        Template template = Template.builder()
                .id(1L)
                .name("Test Template")
                .templateCode("TEST_001")
                .previewFile(previewBlob)
                .build();
        when(templateRepository.findById(1L)).thenReturn(java.util.Optional.of(template));

        // Act
        TemplateRes result = templateService.getTemplateById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Template", result.getName());
        assertEquals("TEST_001", result.getTemplateCode());
        verify(templateRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTemplateById_NotFound() {
        // Arrange
        when(templateRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        TemplateException exception = assertThrows(
                TemplateException.class,
                () -> templateService.getTemplateById(1L)
        );
        assertEquals("Template not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testDownloadTemplateById_Success() throws SQLException {
        // Arrange
        Blob previewBlob = new SerialBlob(new byte[]{1, 2, 3});
        Template template = Template.builder()
                .id(1L)
                .previewFile(previewBlob)
                .build();
        when(templateRepository.findById(1L)).thenReturn(java.util.Optional.of(template));

        // Act
        byte[] result = templateService.downloadTemplateById(1L);

        // Assert
        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result);
        verify(templateRepository, times(1)).findById(1L);
    }

    @Test
    void testDownloadTemplateById_NotFound() {
        // Arrange
        when(templateRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        TemplateException exception = assertThrows(
                TemplateException.class,
                () -> templateService.downloadTemplateById(1L)
        );
        assertEquals("Template not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void downloadTemplateById_shouldThrowTemplateException_whenSQLExceptionOccurs() throws Exception {
        // Arrange
        Long templateId = 1L;

        // Mock Blob and its behavior
        Blob mockBlob = mock(Blob.class);
        when(mockBlob.getBytes(1, (int) mockBlob.length())).thenThrow(new SQLException("Blob reading failed"));

        // Mock Template entity
        Template mockTemplate = Template.builder()
                .id(templateId)
                .previewFile(mockBlob)
                .build();

        // Mock Repository to return the mock Template
        when(templateRepository.findById(templateId)).thenReturn(Optional.of(mockTemplate));

        // Act & Assert
        TemplateException exception = assertThrows(TemplateException.class, () -> {
            templateService.downloadTemplateById(templateId);
        });

        // Assert exception message
        assertTrue(exception.getMessage().contains("Blob reading failed"));
    }

}
