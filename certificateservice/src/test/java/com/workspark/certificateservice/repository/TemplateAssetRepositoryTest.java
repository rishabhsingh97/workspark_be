package com.workspark.certificateservice.repository;

import com.workspark.certificateservice.model.entity.Template;
import com.workspark.certificateservice.model.entity.TemplateAsset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create",
})
class TemplateAssetRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateAssetRepository templateAssetRepository;

    @Test
    void save_ShouldPersistTemplateAsset() throws SQLException {
        // Arrange
        Template template1 = new Template();
        template1.setName("Template 1");
        template1.setTemplateCode("TEST001");

        Blob fileDataBlob1 = new SerialBlob("This is file data".getBytes());
        template1.setFileData(fileDataBlob1);

        Blob previewFileBlob1 = new SerialBlob("This is preview file data".getBytes());
        template1.setPreviewFile(previewFileBlob1);

        // Act
        Template savedTemplate = templateRepository.save(template1);

        TemplateAsset templateAsset = new TemplateAsset();

        Blob assetData1 = new SerialBlob("This is preview file data".getBytes());
        templateAsset.setName("asset_1");
        templateAsset.setAssetData(assetData1);

        templateAsset.setTemplate(savedTemplate);

        // Act
        TemplateAsset savedTemplateAsset = templateAssetRepository.save(templateAsset);

        // Assert
        assertNotNull(savedTemplateAsset.getId());
        assertNotNull(savedTemplateAsset);
        assertEquals(templateAsset.getName(), savedTemplateAsset.getName());
    }

}
