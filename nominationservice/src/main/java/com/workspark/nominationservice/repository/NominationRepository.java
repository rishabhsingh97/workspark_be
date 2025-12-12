package com.workspark.nominationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workspark.nominationservice.model.Nomination;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {
}
