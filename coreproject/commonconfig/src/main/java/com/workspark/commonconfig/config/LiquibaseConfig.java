package com.workspark.commonconfig.config;

import com.workspark.commonconfig.config.applicationConfigProperties.DefaultDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "workspark.liquibase", havingValue = "true", matchIfMissing = true)
public class LiquibaseConfig {

    private final DataSource dataSource; // Default DataSource
    private final SpringLiquibase tenantLiquibase;
    private final MultiTenantConfig multiTenantConfig;
    private final DefaultDataSourceProperties springDefaultDataSource;

    // Cache for initialized tenants
    private final ConcurrentMap<String, Boolean> initializedTenants = new ConcurrentHashMap<>();

    // Cache for tenant-specific DataSources
    private final ConcurrentMap<String, DataSource> tenantDataSourceCache = new ConcurrentHashMap<>();

    /**
     * Initializes the schema for a specific tenant, if not already initialized.
     *
     * @param tenantName the name of the tenant
     * @return true if the initialization is successful
     */
    public void initializeSchema(String tenantName) {
        log.info("Starting tenant schema initialization for tenant: {}", tenantName);

        if (tenantName == null || tenantName.isBlank()) {
            log.error("Tenant name is null or blank. Cannot proceed with schema initialization.");
            throw new IllegalArgumentException("Tenant name is not available.");
        }
        log.info("initializedTenants : {}", initializedTenants);

        // Check if the tenant is already initialized
        if (initializedTenants.containsKey(tenantName)) {
            log.info("Tenant {} is already initialized. Skipping initialization.", tenantName);
        }

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // Step 1: Check if tenant is onboarded
            String tenantDbSchema = isTenantOnboarded(stmt, tenantName);

            if (StringUtils.isBlank(tenantDbSchema)) {
                throw new RuntimeException("Tenant not onboarded: " + tenantName);
            }

            // Step 2: Check if tenant's schema/database exists
            if (!doesDatabaseExist(stmt, tenantDbSchema)) {
                createTenantSchema(connection, tenantDbSchema);
            }

            // Step 3: Initialize tenant schema using Liquibase
            runLiquibaseForTenant(tenantDbSchema);

            // Step 4: Add tenant to the multi-tenant configuration
            multiTenantConfig.addTenantToDataSourceIfNotAlreadyExists(tenantDbSchema);

            // Mark tenant as initialized
            initializedTenants.put(tenantName, true);

        } catch (SQLException | LiquibaseException e) {
            log.error("Error during tenant schema initialization for tenant: {}", tenantName, e);
            throw new RuntimeException(e);
        }

        log.info("Tenant schema initialization process completed for tenant: {}", tenantName);
    }

    private String isTenantOnboarded(Statement stmt, String tenantName) throws SQLException {
        try {
            String query = "SELECT tenant_db_schema FROM workspark.tenant_config WHERE tenant_name = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                pstmt.setString(1, tenantName);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    resultSet.next();
                    return resultSet.getString(1);
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    private boolean doesDatabaseExist(Statement stmt, String tenantDbSchema) throws SQLException {
        String query = "SHOW DATABASES LIKE ?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
            pstmt.setString(1, tenantDbSchema);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void createTenantSchema(Connection connection, String tenantName) throws SQLException {
        String createSchemaQuery = "CREATE SCHEMA IF NOT EXISTS " + tenantName;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createSchemaQuery);
            log.info("Schema successfully created for tenant: {}", tenantName);
        }
    }

    private void runLiquibaseForTenant(String tenantDbSchema) throws LiquibaseException {
        DataSource tenantSpecificDataSource = tenantDataSourceCache.computeIfAbsent(tenantDbSchema, this::createTenantDataSource);

        tenantLiquibase.setDataSource(tenantSpecificDataSource);
        tenantLiquibase.setChangeLog("classpath:db/changelog/tenant-changelog.yaml");
        tenantLiquibase.setDefaultSchema(tenantDbSchema);
        tenantLiquibase.setShouldRun(true);

        tenantLiquibase.afterPropertiesSet();
        log.info("Liquibase initialization completed successfully for tenant: {}", tenantDbSchema);
    }

    private DataSource createTenantDataSource(String tenantDbSchema) {
        log.info("Creating data source for tenant schema: {}", tenantDbSchema);
        String url = springDefaultDataSource.getUrl();
        String datasourceUrl = url.substring(0, url.lastIndexOf("/") + 1);
        log.info("URL: {}", datasourceUrl + tenantDbSchema);
        return DataSourceBuilder.create()
                .url(datasourceUrl + tenantDbSchema)
                .username(springDefaultDataSource.getUsername())
                .password(springDefaultDataSource.getPassword())
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
