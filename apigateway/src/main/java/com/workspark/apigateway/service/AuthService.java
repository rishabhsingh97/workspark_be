package com.workspark.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.models.response.BaseRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service class for handling authentication-related operations.
 * This class is responsible for validating tokens and retrieving user information.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient.Builder webClienbuilder;

    /**
     * Validates the provided token by making a request to the authentication service.
     *
     * @param token The JWT token to be validated
     *
     * @return A Mono containing the AuthResponse object with validation status and user information
     */
    public Mono<BaseRes<String>> validateToken(String token, String tenantName) {

        ObjectMapper objectMapper = new ObjectMapper();
        WebClient webClient = webClienbuilder
                .baseUrl("lb://authenticationservice")
                .build();

        String url = "/auth/public/validate-token";
        log.info("Validating token at url: {}", url);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("token", token)
                        .build())
                .header("X-Tenant", tenantName)
                .header("X-InternalServiceSkipAuth", "true")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(body -> log.info("Raw response body: {}", body))
                .map(body -> {
                    try {
                        return objectMapper.readValue(body, new TypeReference<BaseRes<String>>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to validate token: " + e.getMessage())));
    }

}
