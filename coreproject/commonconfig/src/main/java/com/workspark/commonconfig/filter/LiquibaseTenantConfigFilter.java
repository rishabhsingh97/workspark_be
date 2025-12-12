package com.workspark.commonconfig.filter;

import com.workspark.commonconfig.config.LiquibaseConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "workspark.liquibase", havingValue = "true", matchIfMissing = true)
public class LiquibaseTenantConfigFilter extends OncePerRequestFilter {

    private final LiquibaseConfig liquibaseConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String tenant = request.getHeader("X-Tenant");
        if (Objects.nonNull(tenant)) {
            liquibaseConfig.initializeSchema(tenant);
        }
        else {
            log.warn("No Tenant exists for this request");
        }
        filterChain.doFilter(request, response);
    }
}
