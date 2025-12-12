package com.workspark.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * LogFilter is a global filter that logs request and response details.
 * It implements the GlobalFilter interface from Spring Cloud Gateway.
 */
@Order(0)
@Slf4j
@Component
public class LogFilter implements GlobalFilter {

    /**
     * Filters incoming requests and logs request and response details.
     *
     * @param chain provides a way to delegate to the next filter
     * @return a {@link Mono<Void>} that completes when the filter chain is fully processed
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Generate a unique request ID
        String requestId = UUID.randomUUID().toString();
        exchange.getAttributes().put("X-RequestId", requestId);

        // Log request details
        logRequestDetails(exchange, requestId);

        // Proceed with the filter chain while logging the response
        return chain.filter(exchange).doOnSuccess((r) -> logResponseDetails(exchange, requestId));
    }

    private void logRequestDetails(ServerWebExchange exchange, String requestId) {
        String method = exchange.getRequest().getMethod().toString();
        String path = exchange.getRequest().getURI().getPath();
        String queryParams = exchange.getRequest().getQueryParams().toString();

        log.info("Start Request - Request ID: {}", requestId);
        log.info("Request Method: {}, Request Path: {}, Query Params: {}", method, path, queryParams);
    }

    private void logResponseDetails(ServerWebExchange exchange, String requestId) {
        ServerHttpResponse response = exchange.getResponse();
        log.info("Response Status: {}, Request ID: {}", response.getStatusCode(), requestId);
        log.info("End Request - Request ID: {}", requestId);
    }

}
