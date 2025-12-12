package com.workspark.nominationservice.repository;

import com.workspark.nominationservice.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.workspark.nominationservice.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByType(CategoryType categoryType, Pageable pageable);
}
