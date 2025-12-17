package com.workspark.commonconfig.filter;

import com.workspark.commonconfig.models.pojo.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to manage TenantContext lifecycle for each request.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "workspark.multitenant", havingValue = "true", matchIfMissing = true)
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader("X-Tenant");
        if (tenantId != null) {
            TenantContext.setCurrentTenant(tenantId);
            log.info("Tenant ID '{}' set in TenantContext for the request.", tenantId);
            log.debug("Request URI: {}, Method: {}, Tenant ID: {}", request.getRequestURI(), request.getMethod(), tenantId);
        } else {
            log.warn("No tenant ID found in the request headers. Using default tenant context.");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            log.debug("Clearing TenantContext for the current request lifecycle.");
            TenantContext.clear();
        }
    }
}

