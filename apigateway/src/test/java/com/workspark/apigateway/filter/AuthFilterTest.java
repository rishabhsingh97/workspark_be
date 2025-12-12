package com.workspark.apigateway.filter;

import com.workspark.apigateway.config.AppConfig;
//import com.workspark.apigateway.model.response.AuthResponse;
import com.workspark.apigateway.service.AuthService;
import com.workspark.models.response.BaseRes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    @Mock
    private AppConfig appConfig;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthFilter authFilter;

    @Test
    void testWhitelistedPath_skipsAuthentication() {

        when(appConfig.getWhitelistedPaths()).thenReturn(Set.of("/*/public/**"));

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("https://www.mindfire.workspark.com/auth/public/test")
                        .header("X-Tenant", "mindfire")
                        .build()
        );

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = authFilter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
        verify(authService, never()).validateToken(anyString(), anyString());
    }


    @Test
    void testInvalidJwtToken_ReturnUnauthorizedResponse() {

        // Arrange
        when(appConfig.getWhitelistedPaths()).thenReturn(Set.of("/*/public/**"));
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.options("https://www.workspark.com/auth/api/v1/test")
                        .header("X-Tenant", "mindfire")
                        .build()
        );

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        Mono<Void> result = authFilter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .expectNextMatches(body -> body.contains("Missing or invalid JWT token"))
                .verifyComplete();
        verify(chain, never()).filter(exchange);
    }

    @Test
    void testValidJwtToken_Success() {
        // Arrange
        when(appConfig.getWhitelistedPaths()).thenReturn(Set.of("/*/public/**"));

        // Mock the exchange to simulate an incoming request
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.options("https://www.workspark.com/auth/api/v1/test")
                        .header("X-Tenant", "mindfire")
                        .header("Authorization", "Bearer validToken")
                        .build()
        );

        // Mock token validation success
        when(authService.validateToken("validToken", "mindfire"))
                .thenReturn(
                        Mono.just(BaseRes.
                                <String>builder()
                                        .success(true)
                                        .item("userId123")
                                .build()
                        )
                );

        // Mock the filter chain
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        // Mock the chain filter with the mutated exchange
        when(chain.filter(argThat(exchangeArg -> {
            // Check if the exchange request contains the expected mutated header (X-UserId)
            return exchangeArg.getRequest().getHeaders().containsKey("X-UserId") &&
                    "userId123".equals(exchangeArg.getRequest().getHeaders().getFirst("X-UserId"));
        }))).thenReturn(Mono.empty());

        // Call the filter method
        Mono<Void> result = authFilter.filter(exchange, chain);

        // Assert the filter chain is called exactly once (request should proceed)
        StepVerifier.create(result).verifyComplete();

        // Verify that the chain's filter method is called with the mutated exchange
        verify(chain, times(1)).filter(any());
    }

    @Test
    void testMissingXTenantHeader_FailReturnUnauthorizedResponse() {

        when(appConfig.getWhitelistedPaths()).thenReturn(Set.of("/*/public/**"));

        // Creating a request without the "x-tenant" header
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.options("https://www.workspark.com/auth/api/v1/test")
                        .header("Authorization", "Bearer validToken")
                        .build()
        );

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        Mono<Void> result = authFilter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        StepVerifier.create(exchange.getResponse().getBodyAsString())
                .expectNextMatches(body -> body.contains("Missing required header X-Tenant"))
                .verifyComplete();
        verify(chain, never()).filter(exchange);
    }
}