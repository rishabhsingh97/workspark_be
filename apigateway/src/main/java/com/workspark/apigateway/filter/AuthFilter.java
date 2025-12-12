package com.workspark.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.apigateway.config.AppConfig;
import com.workspark.apigateway.service.AuthService;
import com.workspark.models.response.BaseRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Objects;

/**
 * AuthFilter is a GatewayFilter implementation that handles authentication for incoming requests.
 * It checks for whitelisted APIs, validates JWT tokens, and adds user information to the request headers.
 * If authentication fails, it returns an unauthorized response.
 */
@Order(2)
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter {

    private final AuthService authService;
    private final AppConfig appConfig;

    /**
     * Processes the request through the filter chain.
     *
     * @param exchange the current server exchange
     * @param chain provides a way to delegate to the next filter
     *
     * @return Mono<Void> to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("Processing request for path: {}", path);


        // Skipping for public paths
        if (isWhitelistedApi(exchange.getRequest())) {
            log.info("Whitelisted API detected. Skipping authentication for path: {}", path);
            return chain.filter(exchange);
        }

        // Extract JWT token
        log.info("Extracting JWT token from request for path: {}", path);
        String token = extractJwtFromRequest(exchange.getRequest());
        log.info("token : {}", token);

        if (token == null) {
            log.warn("Missing or invalid JWT token for path: {}", path);
            return unauthorizedResponse(exchange, "Missing or invalid JWT token");
        }

        // Validate token and process asynchronously
        log.info("Validating tenantName");

        String tenantName;
        try {
            tenantName = Objects.requireNonNull(exchange.getRequest().getHeaders().get("X-Tenant")).getFirst();
        }
        catch (Exception e){
            log.error("Missing required header X-Tenant");
            return unauthorizedResponse(exchange, "Missing required header X-Tenant");
        }

        log.info("tenant name : {}", tenantName);

        return authService.validateToken(token, tenantName)
                .flatMap(response -> {
                    if (!response.isSuccess()) {
                        log.warn("Invalid token for user");
                        return unauthorizedResponse(exchange, "Invalid token");
                    }
                    String redisAuthUserId = response.getItem();
                    // Add user information to headers
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-UserId", redisAuthUserId)
                            .build();
                    log.info("Token valid. User ID: {}. Forwarding request.", redisAuthUserId);

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .onErrorResume(e -> {
                    log.error("Error during authentication for path: {}. Cause: {}", path, e.getMessage(), e);
                    return unauthorizedResponse(exchange, e.getMessage());
                });
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * @param request ServerHttpRequest object containing the request details
     *
     * @return String containing the JWT token or null if not found or invalid
     */
    private String extractJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Creates an unauthorized response with the provided error message.
     *
     * @param exchange ServerWebExchange object containing the response details
     * @param message  String containing the error message
     *
     * @return Mono<Void> representing the completion signal
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        BaseRes<String> response = BaseRes.<String>builder()
                .item(null)
                .error("Authentication Error")
                .message(message)
                .build();

        log.warn("Unauthorized response: {}", message);

        return exchange.getResponse().writeWith(Mono.just(
                        exchange.getResponse()
                                .bufferFactory()
                                .wrap(serializeToJson(response).getBytes()))
                )
                .doOnTerminate(() -> log.info("Completed unauthorized response"));
    }


    private String serializeToJson(BaseRes<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Error serializing error response to JSON", e);
            return "{}";
        }
    }

    /**
     * Checks if the request URI matches any of the whitelisted APIs.
     *
     *  @param request ServerHttpRequest object containing the request details
     *
     *  @return boolean indicating if the request is whitelisted
     */
    private boolean isWhitelistedApi(ServerHttpRequest request) {
        log.info("whitelisted apis: {}", appConfig.getWhitelistedPaths());
        log.info("Checking if the request is whitelisted: {}", request.getURI().getPath());
        String requestUri = request.getURI().getPath();
        AntPathMatcher matcher = new AntPathMatcher();
        return appConfig.getWhitelistedPaths().stream().anyMatch(pattern -> matcher.match(pattern, requestUri));
    }

}
