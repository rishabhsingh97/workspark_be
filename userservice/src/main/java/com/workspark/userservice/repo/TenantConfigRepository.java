package com.workspark.userservice.repo;

import com.workspark.userservice.model.entitity.TenantManagementConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantConfigRepository extends JpaRepository<TenantManagementConfig, Long> {
    @Query("SELECT t.tenantDbSchemaName FROM TenantManagementConfig t")
    List<String> findAllTenantDbSchemas();

}
