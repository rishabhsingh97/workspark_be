package com.workspark.certificateservice.repository;

import com.workspark.certificateservice.model.entity.Template;
import com.workspark.certificateservice.model.entity.TemplateDynamicField;
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
class TemplateDynamicFieldRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateDynamicFieldRepository templateDynamicFieldRepository;

    @Test
    void save_ShouldPersistTemplateDynamicField() throws SQLException {
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

        TemplateDynamicField templateDynamicField1 = new TemplateDynamicField();
        templateDynamicField1.setDynamicField("Dynamic Field 1");
        templateDynamicField1.setTemplate(savedTemplate);

        TemplateDynamicField savedTemplateDynamicField = templateDynamicFieldRepository.save(templateDynamicField1);


        // Assert
        assertNotNull(savedTemplateDynamicField.getId());
        assertNotNull(savedTemplateDynamicField);
        assertEquals(templateDynamicField1.getDynamicField(), savedTemplateDynamicField.getDynamicField());
    }
}
