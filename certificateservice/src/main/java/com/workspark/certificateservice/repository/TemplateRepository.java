package com.workspark.certificateservice.repository;

import com.workspark.certificateservice.model.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Template entities.
 * Extends JpaRepository to inherit basic CRUD operations and other functionality.
 */
@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
}
