package com.workspark.apigateway.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantResolverFilterTest {

    @InjectMocks
    private TenantResolverFilter tenantResolverFilter;

    @Test
    void testFilter_withValidHost() {
        // Mock request with a valid Host header
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("https://www.mindfire.workspark.com/auth/public/test")
                        .header("Host", "workspark.mindfire.com")
                        .build()
        );

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(argThat(exchangeArg -> {
            // Check if the exchange request contains the expected mutated header (X-UserId)
            return exchangeArg.getRequest().getHeaders().containsKey("X-Tenant") &&
                    "workspark".equals(exchangeArg.getRequest().getHeaders().getFirst("X-Tenant"));
        }))).thenReturn(Mono.empty());

        // Call the filter method
        Mono<Void> result = tenantResolverFilter.filter(exchange, chain);

        // Verify behavior
        StepVerifier.create(result).verifyComplete();
        verify(chain, times(1)).filter(any());
    }

    @Test
    void testFilter_withInvalidHost() {

        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.options("https://www.workspark.com/auth/api/v1/test")
                        .build()
        );

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        Mono<Void> result = tenantResolverFilter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .expectNextMatches(body -> body.contains("Domain resolution error"))
                .verifyComplete();
        verify(chain, never()).filter(exchange);
    }
}
