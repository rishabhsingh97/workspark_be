package com.workspark.nominationservice.repository;

import com.workspark.nominationservice.model.QuestionCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCategoryMappingRepository extends JpaRepository<QuestionCategoryMapping, Long> {
}
