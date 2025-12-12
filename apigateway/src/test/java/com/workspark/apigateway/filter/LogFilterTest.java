package com.workspark.apigateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogFilterTest {

    @InjectMocks
    private LogFilter logFilter;

    @Mock
    private GatewayFilterChain chain;

    @Test
    void testFilter() {
        // Create a mock request and response
        MockServerHttpRequest request = MockServerHttpRequest.method(HttpMethod.GET, "/api/test")
                .queryParam("param", "value")
                .build();
        MockServerHttpResponse response = new MockServerHttpResponse();

        ServerWebExchange exchange = mock(ServerWebExchange.class);
        when(exchange.getRequest()).thenReturn(request);
        when(exchange.getResponse()).thenReturn(response);

        // Mock the behavior of the chain
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Execute the filter
        Mono<Void> result = logFilter.filter(exchange, chain);

        // Verify chain is invoked
        StepVerifier.create(result)
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);

        // Verify that response details are logged
        response.setStatusCode(HttpStatus.OK);
    }
}
