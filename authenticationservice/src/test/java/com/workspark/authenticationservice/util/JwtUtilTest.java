package com.workspark.authenticationservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.models.enums.UserRole;
import com.workspark.models.pojo.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * Unit tests for the {@link JwtUtil} class.
 * <p>
 * This test class verifies the functionality of JWT token generation,
 * validation, and claim extraction. Each test includes detailed logging for
 * easier debugging and traceability.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

	@InjectMocks
	private JwtUtil jwtUtil;
	
	@Mock
    private ObjectMapper objectMapper;
	
	@Mock
    private OidcUser oidcUser;

    @Mock
    private PublicKey publicKey;

    @Mock
    private Claims claims;

    private final String validToken = "validJwtToken";
    private final String invalidToken = "invalidJwtToken";
    private final String issuer = "https://example.com/";
    

	private final String secret = "testSecret5678901234567890123456789012";
	private final long jwtExpiration = 3600000; // 1 hour
	private final long refreshExpiration = 86400000; // 24 hours

	private final String uuid = "e6c04824-da8c-4869-9861-4150aea526ce";
	private final AuthUser authUser = AuthUser.builder().email("admin@gmail.com").roles(List.of(UserRole.ADMIN))
			.firstName("Test").lastName("Admin").build();

	private final String userAccessTokenSubject = "userAccessToken";

	/**
	 * Set up the test environment by injecting the necessary fields into
	 * {@link JwtUtil}.
	 */
	@BeforeEach
	void setUp() {
		log.info("Initializing JwtUtilTest...");
		ReflectionTestUtils.setField(jwtUtil, "secret", secret);
		ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", jwtExpiration);
		ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpiration", refreshExpiration);
		log.info("JwtUtilTest setup complete with secret: {}, jwtExpiration: {}, refreshExpiration: {}", secret,
				jwtExpiration, refreshExpiration);
	}

	/**
	 * Test to verify successful generation of an access token.
	 * Test to verify successful generation of an access token.
	 */
	@Test
	void testGenerateTokenSuccess() {
		log.info("Running testGenerateTokenSuccess");

		// Act
		log.info("Generating access token for AuthUser: {}", authUser);
		log.info("Generating access token for AuthUser: {}", authUser);
		String token = jwtUtil.generateAccessToken(authUser, uuid);

		// Assert
		log.info("Generated token: {}", token);
		assertNotNull(token, "Token should not be null.");
		assertFalse(token.isEmpty(), "Token should not be empty.");
		log.info("Generated token: {}", token);
		assertNotNull(token, "Token should not be null.");
		assertFalse(token.isEmpty(), "Token should not be empty.");
		log.info("testGenerateTokenSuccess passed successfully.");
	}

	/**
	 * Test to validate a valid JWT token.
	 */
	/**
	 * Test to validate a valid JWT token.
	 */
	@Test
	void testValidateTokenValidToken() {
		log.info("Running testValidateTokenValidToken");


		// Arrange
		String token = jwtUtil.generateAccessToken(authUser, uuid);
		log.info("Generated valid token for validation: {}", token);
		log.info("Generated valid token for validation: {}", token);

		// Act
		boolean isValid = jwtUtil.validateToken(token, userAccessTokenSubject);
		log.info("Validation result: {}", isValid);
		log.info("Validation result: {}", isValid);

		// Assert
		assertTrue(isValid, "Token should be valid.");
		assertTrue(isValid, "Token should be valid.");
		log.info("testValidateTokenValidToken passed successfully.");
	}

	/**
	 * Test to validate a token with an invalid subject.
	 * Test to validate a token with an invalid subject.
	 */
	@Test
	void testValidateTokenInvalidSubject() {
		log.info("Running testValidateTokenInvalidSubject");

		log.info("Running testValidateTokenInvalidSubject");

		// Arrange
		String token = jwtUtil.generateAccessToken(authUser, uuid);
		log.info("Generated token for validation: {}", token);
		log.info("Generated token for validation: {}", token);

		// Act
		boolean isValid = jwtUtil.validateToken(token, "randomTokenType");
		log.info("Validation result with incorrect subject: {}", isValid);
		log.info("Validation result with incorrect subject: {}", isValid);

		// Assert
		assertFalse(isValid, "Token should not be valid with an incorrect subject.");
		log.info("testValidateTokenInvalidSubject passed successfully.");
		assertFalse(isValid, "Token should not be valid with an incorrect subject.");
		log.info("testValidateTokenInvalidSubject passed successfully.");
	}

	/**
	 * Test to verify successful generation of a refresh token.
	 */
	/**
	 * Test to verify successful generation of a refresh token.
	 */
	@Test
	void testGenerateRefreshTokenSuccess() {
		log.info("Running testGenerateRefreshTokenSuccess");

		// Act
		log.info("Generating refresh token for AuthUser: {}", authUser);
		log.info("Generating refresh token for AuthUser: {}", authUser);
		String refreshToken = jwtUtil.generateRefreshToken(authUser, uuid);

		// Assert
		log.info("Generated refresh token: {}", refreshToken);
		assertNotNull(refreshToken, "Refresh token should not be null.");
		assertFalse(refreshToken.isEmpty(), "Refresh token should not be empty.");
		log.info("Generated refresh token: {}", refreshToken);
		assertNotNull(refreshToken, "Refresh token should not be null.");
		assertFalse(refreshToken.isEmpty(), "Refresh token should not be empty.");
		log.info("testGenerateRefreshTokenSuccess passed successfully.");
	}

	/**
	 * Test to extract claims from a token.
	 * Test to extract claims from a token.
	 */
	@Test
	void testExtractClaimsFromToken() {
		log.info("Running testExtractClaimsFromToken");

		// Arrange
		String token = jwtUtil.generateAccessToken(authUser, uuid);
		log.info("Generated token for claim extraction: {}", token);
		log.info("Generated token for claim extraction: {}", token);

		// Act
		String extractedUuid = jwtUtil.extractRedisAuthUserId(token);
		log.info("Extracted UUID from token: {}", extractedUuid);
		log.info("Extracted UUID from token: {}", extractedUuid);

		// Assert
		assertEquals(uuid, extractedUuid, "Extracted UUID should match the original UUID.");
		assertEquals(uuid, extractedUuid, "Extracted UUID should match the original UUID.");
		log.info("testExtractClaimsFromToken passed successfully.");
	}

	/**
	 * Test to check token expiration.
	 * Test to check token expiration.
	 */
	@Test
	void testIsTokenExpired() {
		log.info("Running testIsTokenExpired");

		// Arrange
		ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", -1000L); // Force token expiration
		ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", -1000L); // Force token expiration
		String expiredToken = jwtUtil.generateAccessToken(authUser, uuid);
		log.info("Generated expired token: {}", expiredToken);
		log.info("Generated expired token: {}", expiredToken);

		// Act & Assert
		log.info("Validating that the token is expired...");
		ExpiredJwtException exception = assertThrows(ExpiredJwtException.class,
				() -> jwtUtil.validateToken(expiredToken, userAccessTokenSubject));

		log.info("Caught expected ExpiredJwtException: {}", exception.getMessage());
		assertTrue(exception.getMessage().startsWith("JWT expired at"),
				"Exception message should indicate expiration.");
		log.info("testIsTokenExpired passed successfully.");
	}

	/**
	 * Test generating a new token from an old token.
	 * Test generating a new token from an old token.
	 */
	@Test
	void testGenerateNewTokenFromOldToken() {
		log.info("Running testGenerateNewTokenFromOldToken");

		// Arrange
		String oldToken = jwtUtil.generateAccessToken(authUser, uuid);
		log.info("Generated old token: {}", oldToken);
		log.info("Generated old token: {}", oldToken);

		// Act
		String newToken = jwtUtil.generateNewTokenFromOldToken(oldToken);
		log.info("Generated new token from old token: {}", newToken);
		log.info("Generated new token from old token: {}", newToken);

		// Assert
		assertNotNull(newToken, "New token should not be null.");
		assertFalse(newToken.isEmpty(), "New token should not be empty.");
		assertNotNull(newToken, "New token should not be null.");
		assertFalse(newToken.isEmpty(), "New token should not be empty.");
		log.info("testGenerateNewTokenFromOldToken passed successfully.");
	}

	/**
	 * Test validating a malformed token.
	 */
	@Test
	void testValidateMalformedToken() {
		log.info("Running testValidateMalformedToken");

		// Arrange
		String malformedToken = "invalidToken";
		log.info("Validating malformed token: {}", malformedToken);
		log.info("Validating malformed token: {}", malformedToken);

		// Act & Assert
		log.info("Expecting MalformedJwtException...");
		assertThrows(MalformedJwtException.class, () -> jwtUtil.validateToken(malformedToken, userAccessTokenSubject));
		log.info("testValidateMalformedToken passed successfully.");
	}

	@Test
	void testExtractRedisAuthUserId() {
		log.info("Running testExtractRedisAuthUserId");

		// Arrange
		String token = jwtUtil.generateAccessToken(authUser, uuid);
		log.info("Generated token for extracting Redis Auth User ID: {}", token);

		// Act
		String extractedUuid = jwtUtil.extractRedisAuthUserId(token);
		log.info("Extracted UUID: {}", extractedUuid);

		// Assert
		assertEquals(uuid, extractedUuid, "Extracted UUID should match the original UUID.");
		log.info("testExtractRedisAuthUserId passed successfully.");
	}

	@Test
	void testValidateTokenValid() throws Exception {
		log.info("Running testValidateTokenValid");

		// Arrange
		OidcUser oidcUser = mock(OidcUser.class);
		when(oidcUser.getIssuer()).thenReturn(new URL("https://issuer.example.com")); // Change URI to URL
		String token = jwtUtil.generateAccessToken(authUser, uuid);
		log.info("Generated token for validation: {}", token);

		// Act
		boolean isValid = jwtUtil.validateToken(token, oidcUser);
		log.info("Token validation result: {}", isValid);

		log.info("testValidateTokenValid passed successfully.");
	}

	@Test
	void testValidateTokenInvalid() throws MalformedURLException {
		log.info("Running testValidateTokenInvalid");

		// Arrange
		OidcUser oidcUser = mock(OidcUser.class);
		when(oidcUser.getIssuer()).thenReturn(new URL("https://issuer.example.com"));
		String invalidToken = "invalidToken";
		log.info("Using invalid token for validation: {}", invalidToken);

		// Act
		boolean isValid = jwtUtil.validateToken(invalidToken, oidcUser);
		log.info("Token validation result with invalid token: {}", isValid);

		// Assert
		assertFalse(isValid, "Token should not be valid.");
		log.info("testValidateTokenInvalid passed successfully.");
	}

	@Test
	void testGetKidFromTokenUsingReflection() throws Exception {
		log.info("Running testGetKidFromTokenUsingReflection");

		// Arrange
		String token = jwtUtil.generateAccessToken(authUser, uuid);
		Method getKidMethod = JwtUtil.class.getDeclaredMethod("getKidFromToken", String.class);
		getKidMethod.setAccessible(true); // Make the private method accessible

		// Act
		String kid = (String) getKidMethod.invoke(jwtUtil, token);
		log.info("Extracted kid: {}", kid);

		log.info("testGetKidFromTokenUsingReflection passed successfully.");
	}
}
