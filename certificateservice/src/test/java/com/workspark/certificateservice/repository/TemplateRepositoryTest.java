package com.workspark.certificateservice.repository;

import com.workspark.certificateservice.model.entity.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create",
})
class TemplateRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Test
    void save_ShouldPersistTemplate() throws SQLException {
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

        // Assert
        assertNotNull(savedTemplate.getId());
        assertNotNull(savedTemplate);
        assertEquals(template1.getName(), savedTemplate.getName());
        assertEquals(template1.getTemplateCode(), savedTemplate.getTemplateCode());
    }

    @Test
    void findById_WithExistingId_ShouldReturnTemplate() throws SQLException {
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

        // Act
        Optional<Template> foundTemplate = templateRepository.findById(savedTemplate.getId());

        // Assert
        assertTrue(foundTemplate.isPresent());
        assertEquals(template1.getName(), foundTemplate.get().getName());
        assertEquals(template1.getTemplateCode(), foundTemplate.get().getTemplateCode());
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Act
        Optional<Template> result = templateRepository.findById(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTemplates() throws SQLException {
        // Arrange
        Template template1 = new Template();
        template1.setName("Template 1");
        template1.setTemplateCode("TEST001");

        Blob fileDataBlob1 = new SerialBlob("This is file data".getBytes());
        template1.setFileData(fileDataBlob1);

        Blob previewFileBlob1 = new SerialBlob("This is preview file data".getBytes());
        template1.setPreviewFile(previewFileBlob1);

        templateRepository.save(template1);

        Template template2 = new Template();
        template2.setName("Template 2");
        template2.setTemplateCode("TEST002");

        Blob fileDataBlob2 = new SerialBlob("This is file data".getBytes());
        template2.setFileData(fileDataBlob2);

        Blob previewFileBlob2 = new SerialBlob("This is preview file data".getBytes());
        template2.setPreviewFile(previewFileBlob2);


        templateRepository.save(template2);

        // Act
        List<Template> templates = templateRepository.findAll();

        // Assert
        assertEquals(2, templates.size());
        assertTrue(templates.stream()
                .map(Template::getName)
                .anyMatch(name -> name.equals("Template 1")));
        assertTrue(templates.stream()
                .map(Template::getName)
                .anyMatch(name -> name.equals("Template 2")));
    }

}
