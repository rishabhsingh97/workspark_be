package com.workspark.masterservice.service.serviceImpl;

import com.workspark.masterservice.model.entitiy.Tenant;
import com.workspark.masterservice.service.LiquibaseService;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiquibaseServiceImpl implements LiquibaseService {

    @Override
    public void initTenantSchema(Tenant tenant) {
        String tenantDb = tenant.getDb();

        // ✅ STEP 1: create database first
        createDatabaseIfNotExists(tenant);

        try {
            log.info("Running Liquibase for tenant DB: {}", tenantDb);

            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(createTenantDataSource(tenant));
            liquibase.setChangeLog("classpath:db/changelog/tenant-schema-changelog.yaml");
            liquibase.setShouldRun(true);

            liquibase.afterPropertiesSet();

            log.info("Liquibase completed successfully for tenant: {}", tenantDb);

        } catch (LiquibaseException e) {
            log.error("Liquibase failed for tenant: {}", tenantDb, e);
            throw new RuntimeException("Liquibase execution failed", e);
        }
    }

    @Override
    public void runMigration(Tenant tenant) {
        String tenantDb = tenant.getDb();

        // ✅ STEP 1: create database first
        createDatabaseIfNotExists(tenant);

        try {
            log.info("Running Liquibase for tenant DB: {}", tenantDb);

            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(createTenantDataSource(tenant));
            liquibase.setChangeLog("classpath:db/changelog/tenant-data-changelog.yaml");
            liquibase.setShouldRun(true);

            liquibase.afterPropertiesSet();

            log.info("Liquibase completed successfully for tenant: {}", tenantDb);

        } catch (LiquibaseException e) {
            log.error("Liquibase failed for tenant: {}", tenantDb, e);
            throw new RuntimeException("Liquibase execution failed", e);
        }
    }

    private void createDatabaseIfNotExists(Tenant tenant) {
        String dbName = tenant.getDb();

        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName +
                " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/workspark",
                "root",
                "root");
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            log.info("Database {} created or already exists", dbName);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database " + dbName, e);
        }
    }

    private DataSource createTenantDataSource(Tenant tenant) {
        HikariDataSource ds = new HikariDataSource();

        ds.setJdbcUrl("jdbc:mysql://localhost:3306/" + tenant.getDb() + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("root");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return ds;
    }
}
