package com.workspark.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * A filter to resolve domain names from incoming requests and process them dynamically.
 *
 * <p>This filter:
 * <ul>
 *   <li>Resolves domain names based on the request path.</li>
 *   <li>Injects resolved domain or user information into the request headers.</li>
 * </ul>
 */
@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantResolverFilter implements GlobalFilter  {

    /**
     * Filters incoming requests, resolves domains, and injects user information.
     *
     * @param exchange the incoming {@link ServerRequest}
     * @param chain    the next handler in the chain
     * @return a {@link Mono<Void>}
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            String tenant = resolveTenant(exchange.getRequest());
            log.info("Resolved tenant: {}", tenant);

            if (tenant == null) {
                return unauthorizedResponse(exchange);
            }

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-Tenant", tenant)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }


    /**
     * Resolves the tenant from the request path or headers.
     *
     * @param request the incoming {@link ServerRequest}
     * @return the resolved domain name
     */
    private String resolveTenant(ServerHttpRequest request) {
        // Extract the 'Host' header
        String host = request.getHeaders().getFirst(HttpHeaders.HOST);
        log.info("tenant: {}", host);

        if (host != null && !host.isEmpty()) {
            String tenant = host.split(":")[0]; // ignoring port if present
            tenant = tenant.split("\\.")[0]; // get subdomain value
            return tenant;
        }

        log.warn("Host header is missing in the request");
        return null;
    }


    /**
     * Creates an unauthorized response with the provided error message.
     *
     * @param exchange ServerWebExchange object containing the response details
     * @return Mono<Void> representing the completion signal
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        BaseRes<String> response = BaseRes.<String>builder()
                .item(null)
                .error("Authentication Error")
                .message("Domain resolution error")
                .build();

        log.warn("Unauthorized response: {}", "Domain resolution error");

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
}
