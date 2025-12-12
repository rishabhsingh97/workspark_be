package com.workspark.authenticationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.workspark.authenticationservice.client.UserServiceClient;
import com.workspark.authenticationservice.handler.CustomOAuth2AuthenticationSuccessHandler;
import com.workspark.authenticationservice.util.JwtUtil;
import com.workspark.commonconfig.service.RedisAuthUserService;

@Primary
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserServiceClient userServiceClient;
	private final RedisAuthUserService redisAuthUserService;

	public SecurityConfig(JwtUtil jwtUtil,
			UserServiceClient userServiceClient, RedisAuthUserService redisAuthUserService) {
		this.jwtUtil = jwtUtil;
		this.userServiceClient = userServiceClient;
		this.redisAuthUserService = redisAuthUserService;
	}

	@Bean
	public CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler() {
		return new CustomOAuth2AuthenticationSuccessHandler(jwtUtil, userServiceClient,redisAuthUserService);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.oauth2Login(oauth -> oauth
					.successHandler(new CustomOAuth2AuthenticationSuccessHandler(jwtUtil, userServiceClient,redisAuthUserService))
				);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}