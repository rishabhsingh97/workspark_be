package com.workspark.nominationservice.repository;

import com.workspark.nominationservice.model.CategoryPanelistMapping;
import com.workspark.nominationservice.model.CategoryPanelistMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryPanelistMappingRepository extends JpaRepository<CategoryPanelistMapping, CategoryPanelistMappingId> {
    List<CategoryPanelistMapping> findByRecognitionCategoryCategoryId(Long categoryId);
}

