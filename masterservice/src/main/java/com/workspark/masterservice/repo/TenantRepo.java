package com.workspark.masterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workspark.masterservice.model.entitiy.Tenant;

@Repository
public interface TenantRepo extends JpaRepository<Tenant, Long> {
}
