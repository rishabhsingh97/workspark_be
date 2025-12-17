package com.workspark.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The main class for the API Gateway application.
 *
 * <p>This class serves as the entry point for the Spring Boot application.
 * It enables service discovery and configures the application to run as a Spring Boot application.</p>
 */
//@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
public class ApigatewayApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
    }
}
