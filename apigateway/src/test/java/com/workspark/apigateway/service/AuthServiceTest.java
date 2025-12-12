package com.workspark.apigateway.service;

//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

//    @Mock
//    private WebClient.Builder webClientBuilder;
//
//    @Mock
//    private WebClient webClient;
//
//    @Mock
//    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
//
//    @Mock
//    private WebClient.RequestHeadersSpec requestHeadersSpec;
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//    private AuthService authService;
//
//    @BeforeEach
//    void setUp() {
//        authService = new AuthService(webClientBuilder);
//    }

//    @Test
//    void validateToken_WhenValidToken_ShouldReturnAuthResponse() {
//        // Arrange
//        String token = "valid.jwt.token";
//        String tenantName = "test-tenant";
//        String expectedResponse = n();
//
//        setupWebClientMock(Mono.just(expectedResponse));
//
//        // Act & Assert
//        StepVerifier.create(authService.validateToken(token, tenantName))
//                .expectNext(expectedResponse)
//                .verifyComplete();
//    }

//    @Test
//    void validateToken_WhenInvalidToken_ShouldReturnError() {
//        // Arrange
//        String token = "invalid.token";
//        String tenantName = "test-tenant";
//        RuntimeException originalError = new RuntimeException("Invalid token");
//
//        setupWebClientMock(Mono.error(originalError));
//
//        // Act & Assert
//        StepVerifier.create(authService.validateToken(token, tenantName))
//                .expectErrorMatches(throwable ->
//                        throwable instanceof RuntimeException &&
//                                throwable.getMessage().equals("Failed to validate token") &&
//                                throwable.getCause() == originalError)
//                .verify();
//    }

//    @Test
//    void validateToken_WhenNetworkError_ShouldReturnError() {
//        // Arrange
//        String token = "valid.jwt.token";
//        String tenantName = "test-tenant";
//        RuntimeException networkError = new RuntimeException("Network connection failed");
//
//        setupWebClientMock(Mono.error(networkError));
//
//        // Act & Assert
//        StepVerifier.create(authService.validateToken(token, tenantName))
//                .expectErrorMatches(throwable ->
//                        throwable instanceof RuntimeException &&
//                                throwable.getMessage().equals("Failed to validate token") &&
//                                throwable.getCause() == networkError)
//                .verify();
//    }

//    private void setupWebClientMock(Mono<AuthResponse> response) {
//        when(webClientBuilder.baseUrl("lb://authenticationservice")).thenReturn(webClientBuilder);
//        when(webClientBuilder.build()).thenReturn(webClient);
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(AuthResponse.class)).thenReturn(response);
//    }
}
