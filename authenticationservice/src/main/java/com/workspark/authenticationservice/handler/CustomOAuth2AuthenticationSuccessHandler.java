package com.workspark.authenticationservice.handler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.SignInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.authenticationservice.client.UserServiceClient;
import com.workspark.authenticationservice.constant.Constants;
import com.workspark.authenticationservice.exceptions.customExceptions.RedisConnectionException;
import com.workspark.authenticationservice.model.AuthenticationResponse;
import com.workspark.authenticationservice.util.JwtUtil;
import com.workspark.commonconfig.models.entity.RedisAuthUser;
import com.workspark.commonconfig.service.RedisAuthUserService;
import com.workspark.models.enums.UserRole;
import com.workspark.models.pojo.AuthUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Value("${jwt.expiration}")
	private long jwtExpiration;
	
//	@Value("${redirectUrl}")
//	private String redirectUrl;

	private final JwtUtil jwtUtil;
	private final UserServiceClient userServiceClient;
	private final RedisAuthUserService redisAuthUserService;
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	public CustomOAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, UserServiceClient userServiceClient,
			RedisAuthUserService redisAuthUserService) {
		this.jwtUtil = jwtUtil;
		this.userServiceClient = userServiceClient;
		this.redisAuthUserService = redisAuthUserService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("----- Inside CustomOAuth2AuthenticationSuccessHandler-------{}");

		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
		String tenantName = request.getHeader("X-Tenant");
		// Extract user details
		String email = oauth2Token.getPrincipal().getAttribute("email");
		String name = oauth2Token.getPrincipal().getAttribute("name");

		// Log user details or perform custom actions
		System.out.println("OAuth Login Successful!");
		System.out.println("User Email: " + email);
		System.out.println("User Name: " + name);

		OidcUser oidcUser = (OidcUser) oauth2Token.getPrincipal();
		log.info("oidcUser - {}", oidcUser);

		String accessToken = oidcUser.getIdToken().getTokenValue();

		log.info("---- accessToken -----{}", accessToken);

		String userKey = UUID.randomUUID().toString();

		// Validate the token using JwtUtil
		boolean isTokenValid = jwtUtil.validateToken(accessToken, oidcUser);

		if (isTokenValid) {
			log.info("Token is valid. User authentication is successful.");
		} else {
			log.error("Token validation failed.");
			response.sendRedirect("/error");
			return;
		}

		AuthUser authUser = new AuthUser();
		authUser.setFirstName(name);
		authUser.setEmail(email);
		authUser.setRoles(List.of(UserRole.USER));

		String generatedToken = jwtUtil.generateAccessToken(authUser, userKey);
		log.info("---- generatedToken ----- {}", generatedToken);

		BaseRes<SignInResponse> responseEntity = userServiceClient.getUser("email", email);
		if (Objects.nonNull(responseEntity) && responseEntity.isSuccess()) {
			SignInResponse user = responseEntity.getItem();

			if (Objects.nonNull(user)) {

				try {
					// Saving data to redis
					RedisAuthUser redisAuthUser = RedisAuthUser.builder().redisUserId(userKey).authUser(authUser)
							.build();

					// Store user details in Redis with a TTL of 1 hour
					redisAuthUserService.saveUser(redisAuthUser);
					log.info("User {} successfully signed in and details stored in Redis",
							user.getEmail() + " " + user.getFirstName());
				} catch (Exception e) {
					log.error("Unable to connect to Redis: {}", e.getMessage());
					throw new RedisConnectionException(Constants.UNABLE_TO_CONNECT_TO_REDIS);
				}
				// Saving data to userReq
				SignupRequest userReq = SignupRequest.builder().firstName(authUser.getFirstName()).email(authUser.getEmail())
						.roles(authUser.getRoles()).sso(true).build();

				// Creating user and saving userReq to db.
				userServiceClient.createUser(userReq);
				log.info("User {} successfully registered", userReq.getEmail());

			}

		}
		AuthenticationResponse resp = new AuthenticationResponse(generatedToken, null);
		log.info("--generatedToken - {} ",generatedToken);
		 // Set the JWT token as a cookie

        response.sendRedirect("http://localhost:4200/auth/auto_login?token="+ generatedToken);

        // Set the response status to 200 OK
        response.setStatus(HttpServletResponse.SC_OK);
    }
}