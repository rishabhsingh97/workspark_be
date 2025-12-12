package com.workspark.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for setting up the WebClient bean.
 * This class is annotated with @Configuration to indicate that it provides
 * one or more @Bean methods for the Spring container to manage.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a WebClient.Builder bean with load balancing enabled.
     * This helps in resolving relative discovery url for service.
     * This bean is marked as the primary bean, which means it will be used
     * when there are multiple instances of the same type.
     *
     * @return WebClient.Builder instance with load balancing enabled
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
