package com.workspark.nominationservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.workspark.nominationservice.model.RecognitionTier;
import org.springframework.stereotype.Repository;

@Repository
public interface RecognitionTierRepository extends JpaRepository<RecognitionTier, Long> {
}
