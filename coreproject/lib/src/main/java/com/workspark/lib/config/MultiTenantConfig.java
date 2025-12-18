package com.workspark.lib.config;

import com.workspark.lib.config.applicationConfigProperties.DefaultDataSourceProperties;
import com.workspark.lib.models.pojo.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multi-tenant configuration for managing and dynamically switching between tenant-specific data sources.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "workspark.multitenant", havingValue = "true", matchIfMissing = true)
public class MultiTenantConfig {

    private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
    private final DefaultDataSourceProperties springDefaultDataSource;
    private AbstractRoutingDataSource multiTenantDataSource;

    /**
     * Configures and returns the multi-tenant DataSource bean.
     * The DataSource switches dynamically based on the tenant ID stored in ThreadLocal.
     */
    @Bean
    public DataSource dataSource() {
        multiTenantDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                String tenant = TenantContext.getCurrentTenant();
                String tenant_db = "workspark";
                log.info("Current tenant resolved: {}", tenant);
                if(!StringUtils.isBlank(tenant)) {
                    tenant_db = getTenantDatabase(tenant);
                }
                log.info("Current tenant_db resolved: {}", tenant_db);
                try {
                    addTenantToDataSourceIfNotAlreadyExists(tenant_db);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return tenant_db;
            }
        };
        multiTenantDataSource.setTargetDataSources(tenantDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
        multiTenantDataSource.afterPropertiesSet();
        log.info("Multi-tenant DataSource initialized with default DataSource.");
        return multiTenantDataSource;
    }


    /**
     * Adds a new tenant's DataSource to the multi-tenant configuration if it does not already exist.
     *
     * @param tenantDbSchema the tenant ID
     * @throws SQLException if unable to validate the DataSource connection
     */
    private void addTenantToDataSourceIfNotAlreadyExists(String tenantDbSchema) throws SQLException {
        if (tenantDataSources.containsKey(tenantDbSchema)) {
            log.info("Tenant '{}' already exists. Skipping addition.", tenantDbSchema);
            return;
        }

        log.info("Initializing DataSource for tenant '{}'.", tenantDbSchema);
        String dataSourceBaseUrl = springDefaultDataSource.getUrl().substring(0, springDefaultDataSource.getUrl().lastIndexOf("/") + 1);
        String tenantDbUrl = dataSourceBaseUrl.concat(tenantDbSchema);
        log.debug("Constructed tenant database URL: {}", tenantDbUrl);

        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(springDefaultDataSource.getDriverClassName())
                .url(tenantDbUrl)
                .username(springDefaultDataSource.getUsername())
                .password(springDefaultDataSource.getPassword())
                .build();

        // Validate the connection to the new tenant's database
        try (Connection connection = dataSource.getConnection()) {
            log.info("Successfully connected to tenant schema '{}'.", tenantDbSchema);
            tenantDataSources.put(tenantDbSchema, dataSource);
            multiTenantDataSource.afterPropertiesSet();
            log.info("Tenant schema'{}' added to the multi-tenant configuration.", tenantDbSchema);
        } catch (SQLException e) {
            log.error("Failed to connect to tenant '{}'. Tenant not added.", tenantDbSchema, e);
            throw e;
        }
    }

    private String getTenantDatabase(String tenantName) {
        String tenantDbSchema = "";
        String query = "SELECT t.db FROM workspark.tenant t WHERE t.domain = ?";
        try (Connection connection = defaultDataSource().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            log.info("Executing query: {}", query);
            pstmt.setString(1, tenantName);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    tenantDbSchema = resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            log.error("Error retrieving tenant database schema for tenant '{}'.", tenantName, e);
        }
        log.info("Executing query: {}, fetched tenant db: {}", query, tenantDbSchema);
        return tenantDbSchema;
    }

    /**
     * Creates and configures the default DataSource.
     * This is used when no specific tenant ID is resolved.
     *
     * @return the default DataSource
     */
    private DriverManagerDataSource defaultDataSource() {
        log.info("Initializing default DataSource for the application.");
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName(springDefaultDataSource.getDriverClassName());
        defaultDataSource.setUrl(springDefaultDataSource.getUrl());
        defaultDataSource.setUsername(springDefaultDataSource.getUsername());
        defaultDataSource.setPassword(springDefaultDataSource.getPassword());
        return defaultDataSource;
    }
}
