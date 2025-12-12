package com.workspark.nominationservice.repository;

import com.workspark.nominationservice.model.CategoryNominatorMappingId;
import com.workspark.nominationservice.model.CategoryNominatorsMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryNominatorsMappingRepository extends JpaRepository<CategoryNominatorsMapping, CategoryNominatorMappingId> {
    List<CategoryNominatorsMapping> findByRecognitionCategoryCategoryId(Long categoryId);

    Page<CategoryNominatorsMapping> findByIdNominatedBy(Long nominatedBy, Pageable pageable);
}
