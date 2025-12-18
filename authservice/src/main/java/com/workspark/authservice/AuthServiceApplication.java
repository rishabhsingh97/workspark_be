package com.workspark.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The main entry point for the AuthenticationService application.
 * This class initializes the Spring Boot application with the exclusion of 
 * default security configuration and enables Feign clients for service communication.
 * 
 * The application starts the Authentication Service, which handles user authentication 
 * functionalities including login, sign up, password recovery, and token management.
 * 
 * @author mridulj
 * 
 */
@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableJpaRepositories
public class AuthServiceApplication {

	/**
     * The main method to run the Spring Boot AuthenticationService application.
     * It launches the application by calling SpringApplication.run.
     *
     * @param args command-line arguments.
     */
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
