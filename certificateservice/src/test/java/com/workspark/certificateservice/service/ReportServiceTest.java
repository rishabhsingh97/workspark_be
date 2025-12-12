package com.workspark.certificateservice.service;

import com.workspark.certificateservice.exceptions.customExceptions.JasperException;
import com.workspark.certificateservice.model.entity.TemplateAsset;
import com.workspark.certificateservice.service.impl.ReportServiceJasperImpl;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    private ReportServiceJasperImpl reportService;

    private final String jrXmlContent =
            """
                        <jasperReport name="testing" language="java" columnCount="1" pageWidth="871" pageHeight="624" orientation="Landscape" whenNoDataType="AllSectionsNoDetail" columnWidth="871" leftMargin="0" rightMargin="0" topMargin="0" bottomMargin="0" uuid="4606ee64-ef33-409d-9898-fe031641bbff">
                            <property name="net.sf.jasperreports.awt.ignore.missing.font" value="true"/>
                            <property name="net.sf.jasperreports.default.font.name" value="Sans Serif"/>
                            <parameter name="name" class="java.lang.String">
                                <defaultValueExpression><![CDATA[$P{name}]]></defaultValueExpression>
                            </parameter>
                            <query language="sql"><![CDATA[]]></query>
                            <background height="624" splitType="Stretch">
                                <element kind="textField" uuid="15a3e143-ace8-49b8-8000-f7eae3eb16d1" x="-2" y="198" width="873" height="117" forecolor="#323D55" markup="none" fontName="GreatVibes" fontSize="70.0" pdfFontName="GreatVibes" pdfEncoding="Identity-H" pdfEmbedded="true" hTextAlign="Center" vTextAlign="Middle">
                                    <expression><![CDATA[$P{name}]]></expression>
                                </element>
                                <property name="com.jaspersoft.studio.unit.height" value="px"/>
                                <property name="com.jaspersoft.studio.layout"/>
                            </background>
                        </jasperReport>
                    """;

    @Test
    void testCompileTemplateFile() {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("file", "template.jrxml", "text/xml", jrXmlContent.getBytes());

        // Act
        byte[] compiledReport = reportService.compileTemplateFile(mockFile);

        // Assert
        assertNotNull(compiledReport, "The compiled report should not be null");
    }

    @Test
    void testGenerateCertificatePdf() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile("file", "template.jrxml", "text/xml", jrXmlContent.getBytes());

        // Arrange
        byte[] pdfSourceData = reportService.compileTemplateFile(mockFile); // Mock compiled report data
        Map<String, Object> dynamicFieldsData = Map.of("name", "John Doe");

        TemplateAsset mockAsset = mock(TemplateAsset.class);
        Blob mockBlob = mock(Blob.class);

        when(mockAsset.getName()).thenReturn("logo");
        when(mockAsset.getAssetData()).thenReturn(mockBlob);
        when(mockBlob.length()).thenReturn((long) "mock-data".length());
        when(mockBlob.getBytes(1, (int) mockBlob.length())).thenReturn("mock-data".getBytes());

        List<TemplateAsset> assets = List.of(mockAsset);

        // Act
        byte[] pdf = reportService.generateCertificatePdf(pdfSourceData, dynamicFieldsData, assets);

        // Assert
        assertNotNull(pdf, "Generated PDF should not be null");
        assertTrue(pdf.length > 0, "Generated PDF should have content");
    }

    @Test
    void testGenerateCertificateImage() throws Exception {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("file", "template.jrxml", "text/xml", jrXmlContent.getBytes());

        // Arrange
        byte[] pdfSourceData = reportService.compileTemplateFile(mockFile); // Mock compiled report data
        Map<String, Object> dynamicFieldsData = Map.of("title", "Certificate");

        TemplateAsset mockAsset = mock(TemplateAsset.class);
        Blob mockBlob = mock(Blob.class);

        when(mockAsset.getName()).thenReturn("background");
        when(mockAsset.getAssetData()).thenReturn(mockBlob);
        when(mockBlob.length()).thenReturn((long) jrXmlContent.length());
        when(mockBlob.getBytes(1, (int) mockBlob.length())).thenReturn(jrXmlContent.getBytes());

        List<TemplateAsset> assets = List.of(mockAsset);
        String imageType = "jpg";

        // Act
        byte[] image = reportService.generateCertificateImage(pdfSourceData, dynamicFieldsData, assets, imageType);

        // Assert
        assertNotNull(image, "Generated image should not be null");
        assertTrue(image.length > 0, "Generated image should have content");
    }

    @Test
    void testCompileTemplateFileThrowsException() {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile("file", "template.jrxml", "text/xml", new byte[0]);

        // Act & Assert
        JasperException exception = assertThrows(
                JasperException.class,
                () -> reportService.compileTemplateFile(invalidFile),
                "Expected JasperException for empty input"
        );
        assertTrue(exception.getMessage().contains("Error compiling"), "Exception message should indicate compilation error");
    }

    @Test
    void generateCertificateImage_shouldThrowJasperException_whenExceptionOccurs() throws Exception {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("file", "template.jrxml", "text/xml", jrXmlContent.getBytes());

        // Compile the template to get the PDF source data
        byte[] pdfSourceData = reportService.compileTemplateFile(mockFile); // Mock compiled report data

        // Use a mutable map for dynamicFieldsData
        Map<String, Object> dynamicFieldsData = new HashMap<>();
        dynamicFieldsData.put("title", "Certificate");

        // Mock TemplateAsset and Blob
        TemplateAsset mockAsset = mock(TemplateAsset.class);
        Blob mockBlob = mock(Blob.class);

        when(mockAsset.getName()).thenReturn("background");
        when(mockAsset.getAssetData()).thenReturn(mockBlob);
        when(mockBlob.length()).thenReturn((long) jrXmlContent.length());
        when(mockBlob.getBytes(1, (int) mockBlob.length())).thenReturn(jrXmlContent.getBytes());

        List<TemplateAsset> assets = List.of(mockAsset);
        String imageType = "jpg";

        // Mock behavior for JasperFillManager to throw an exception
        when(JasperFillManager.fillReport(any(InputStream.class), eq(dynamicFieldsData), any(JREmptyDataSource.class)))
                .thenThrow(new JRException("Report generation error"));

        // Act & Assert
        JasperException exception = assertThrows(JasperException.class, () -> {
            reportService.generateCertificateImage(pdfSourceData, dynamicFieldsData, assets, imageType);
        });

        assertTrue(exception.getMessage().contains("Error generating certificate image"), "Exception message should indicate the error");
    }


}
