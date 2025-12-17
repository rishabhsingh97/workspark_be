package com.workspark.security.filter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.workspark.commonconfig.models.entity.RedisAuthUser;
import com.workspark.commonconfig.service.RedisAuthUserService;
import com.workspark.models.enums.UserRoleEnum;
import com.workspark.models.response.BaseRes;
import com.workspark.security.model.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Filter for handling authentication based on the "X-UserId" header.
 * This filter checks if the request is whitelisted or contains an internal skip-auth header.
 * If neither condition is met, it validates the user ID against Redis and sets the authentication context.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "workspark.filter", havingValue = "true", matchIfMissing = true)
public class UserIdFilter extends OncePerRequestFilter {

    // Service to fetch RedisAuthUser
    private final RedisAuthUserService redisAuthUserService;

    // APIs that do not require authentication
    private final List<String> whitelistedApis = List.of(
            "/*/public/**"
    );

    // ObjectMapper for writing JSON error responses
    private final ObjectMapper objectMapper;

    /**
     * Main filter logic that processes incoming requests.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain for processing further filters.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        log.info("Processing request: {}", requestUri);

        // Bypass authentication for whitelisted APIs
        if (isWhitelistedApi(request)) {
            log.debug("Request matches whitelisted API, bypassing authentication: {}", requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        // Skip authentication if the internal skip-auth header is present and true
        if (internalServiceSkipAuthHeaderIsTrue(request)) {
            AuthUser systemUser = new AuthUser();
            systemUser.setFirstName("System");
            systemUser.setLastName("User");
            systemUser.setRoles(List.of(UserRoleEnum.SYSTEM));

            if (Objects.nonNull(SecurityContextHolder.getContext()) &&
                    Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(systemUser, null, systemUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication set for user: {}", systemUser.getUsername());
            } else {
                log.debug("Security context already contains an authentication object.");
            }
            log.info("Request contains 'X-InternalServiceSkipAuth' header with value 'true', bypassing authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        // Retrieve the "X-UserId" header
        String authUserId = request.getHeader("X-UserId");
        Enumeration<String> headerNames =request.getHeaderNames();
        // Print headers to the response
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("Header: {} = {}", headerName, headerValue);
        }
        log.info("Extracted 'X-UserId' header: {}", authUserId);

        if (Objects.isNull(authUserId) || authUserId.isEmpty()) {
            log.warn("Missing or empty 'X-UserId' header in request: {}", requestUri);
            sendJsonErrorResponse(response, "Missing or empty 'X-UserId' header");
            return;
        }

        // Fetch user details from Redis
        Optional<RedisAuthUser> redisAuthUser = redisAuthUserService.getUser(authUserId);

        if (redisAuthUser.isPresent()) {
            AuthUser user = new AuthUser();
            log.debug("Found RedisAuthUser for 'X-UserId': {}", authUserId);

            // Set authentication if not already set in the SecurityContext
            if (Objects.nonNull(SecurityContextHolder.getContext()) &&
                    Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Authentication set for user: {}", user.getUsername());
            } else {
                log.debug("Security context already contains an authentication object.");
            }
        } else {
            log.warn("No RedisAuthUser found for 'X-UserId': {}", authUserId);
            sendJsonErrorResponse(response, "No user found for 'X-UserId'");
            return;
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the request matches any whitelisted API pattern.
     *
     * @param request The HTTP request.
     * @return True if the request is whitelisted, false otherwise.
     */
    private boolean isWhitelistedApi(HttpServletRequest request) {
        log.debug("Checking if the request is whitelisted: {}", request.getRequestURI());
        String requestUri = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        return whitelistedApis.stream().anyMatch(pattern -> matcher.match(pattern, requestUri));
    }

    /**
     * Checks if the request contains the internal skip-auth header with a value of "true".
     *
     * @param request The HTTP request.
     * @return True if the skip-auth header is set to "true", false otherwise.
     */
    private boolean internalServiceSkipAuthHeaderIsTrue(HttpServletRequest request) {
        String skipAuthHeader = request.getHeader("X-InternalServiceSkipAuth");
        boolean isSkipAuth = Boolean.parseBoolean(skipAuthHeader);
        log.debug("'X-InternalServiceSkipAuth' header value: {}, parsed as: {}", skipAuthHeader, isSkipAuth);
        return isSkipAuth;
    }

    /**
     * Sends a JSON error response with a specified message.
     *
     * @param response The HTTP response.
     * @param message  The error message.
     * @throws IOException If an I/O error occurs.
     */
    private void sendJsonErrorResponse(HttpServletResponse response, String message) throws IOException {
        log.error("Sending error response: {}", message);
        ResponseEntity<BaseRes<Serializable>> responseEntity = BaseRes.error(message, "Auth Error", HttpStatus.UNAUTHORIZED);
        response.setContentType("application/json");
        response.setStatus(responseEntity.getStatusCode().value());
        objectMapper.writeValue(response.getWriter(), responseEntity.getBody());
    }
}
