package com.workspark.nominationservice.repository;

import com.workspark.nominationservice.model.RecognitionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecognitionCategoryRepository extends JpaRepository<RecognitionCategory, Long> {

}
