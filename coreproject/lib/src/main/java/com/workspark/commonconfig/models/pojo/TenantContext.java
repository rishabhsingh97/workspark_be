package com.workspark.commonconfig.models.pojo;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to manage the current tenant in a ThreadLocal variable.
 */
@Slf4j
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    private TenantContext() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    public static void setCurrentTenant(String tenantId) {
        currentTenant.remove();
        log.debug("Setting current tenant to '{}'.", tenantId);
        currentTenant.set(tenantId);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        log.debug("Clearing current tenant from ThreadLocal.");
        currentTenant.remove();
    }
}
