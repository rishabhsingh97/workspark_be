package com.workspark.security.config;

import com.workspark.security.filter.UserIdFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for setting up security-related configurations.
 * This class configures the security filter chain and adds the UserIdFilter to the filter chain.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@ConditionalOnProperty(name = "workspark.security", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {

    private final UserIdFilter userIdFilter;

    /**
     * Configures the security filter chain.
     * Disables CSRF, disables form login, sets session management policy to stateless,
     * and adds the UserIdFilter before the UsernamePasswordAuthenticationFilter.
     * @param http HttpSecurity object for configuring security settings.
     * @return SecurityFilterChain object representing the configured security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(userIdFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}