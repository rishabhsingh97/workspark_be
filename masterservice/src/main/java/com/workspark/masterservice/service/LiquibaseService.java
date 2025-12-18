package com.workspark.masterservice.service;

import com.workspark.masterservice.model.entitiy.Tenant;

public interface LiquibaseService {
    void initTenantSchema(Tenant tenant);
    void runMigration(Tenant tenant);
}
