package com.workspark.nominationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.workspark.nominationservice.model.NominationQuestionAnswerMapping;
import org.springframework.stereotype.Repository;

@Repository
public interface NominationQuestionAnswerMappingRepository extends JpaRepository<NominationQuestionAnswerMapping, Long> {
}
