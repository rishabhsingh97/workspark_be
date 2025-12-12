package com.workspark.authenticationservice.util;

import java.net.URL;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.commonconfig.models.pojo.TenantContext;
import com.workspark.models.enums.UserRole;
import com.workspark.models.pojo.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for generating, validating, and extracting information from
 * JSON Web Tokens (JWT). Supports both access tokens and refresh tokens, with
 * methods to handle claims, expiration, and token validation.
 *
 * @author mridulj
 */
@Slf4j
@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long jwtExpiration;

	@Value("${jwt.refreshExpiration}")
	private long refreshTokenExpiration;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Generates a JWT access token for the given user.
	 *
	 * @param authUser The authenticated user.
	 * @param redisAuthUserId UUID stored in Redis.
	 * @return A JWT access token as a string.
	 */
	public String generateAccessToken(AuthUser authUser, String redisAuthUserId) {
		log.info("Generating Access Token for user: {}", authUser.getEmail());
		return buildToken(createClaims(authUser, redisAuthUserId), "userAccessToken", jwtExpiration);
	}

	/**
	 * Generates a JWT refresh token for the given user.
	 *
	 * @param authUser The authenticated user.
	 * @param redisAuthUserId UUID stored in Redis.
	 * @return A JWT refresh token as a string.
	 */
	public String generateRefreshToken(AuthUser authUser, String redisAuthUserId) {
		log.info("Generating Refresh Token for user: {}", authUser.getEmail());
		return buildToken(createClaims(authUser, redisAuthUserId), "userRefreshToken", refreshTokenExpiration);
	}

	/**
	 * Validates the token by comparing its type and checking expiration.
	 *
	 * @param token The JWT token.
	 * @param tokenType The expected token type (e.g., "userAccessToken").
	 * @return True if the token is valid, otherwise false.
	 */
	public Boolean validateToken(String token, String tokenType) {
		log.info("Validating token of type: {}", tokenType);
		String subject = extractSubject(token);
		return tokenType.equals(subject) && !isTokenExpired(token);
	}

	/**
	 * Extracts a claim from the token using a resolver function.
	 *
	 * @param token The JWT token.
	 * @param claimsResolver A function to extract the specific claim.
	 * @param <T> The type of the claim.
	 * @return The extracted claim.
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		return claimsResolver.apply(extractAllClaims(token));
	}

	/**
	 * Extracts all claims from the token.
	 *
	 * @param token The JWT token.
	 * @return A Claims object containing all claims.
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Extracts the subject from the token.
	 *
	 * @param token The JWT token.
	 * @return The subject.
	 */
	private String extractSubject(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extracts the expiration date from the token.
	 *
	 * @param token The JWT token.
	 * @return The expiration date.
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Checks if the token has expired.
	 *
	 * @param token The JWT token.
	 * @return True if the token has expired, otherwise false.
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Generates a new JWT access token from an existing token.
	 *
	 * @param token The existing JWT token.
	 * @return A new JWT access token.
	 */
	public String generateNewTokenFromOldToken(String token) {
		return buildToken(extractClaimsFromToken(token), "userAccessToken", jwtExpiration);
	}

	/**
	 * Generates a new JWT refresh token from an existing token.
	 *
	 * @param token The existing JWT token.
	 * @return A new JWT refresh token.
	 */
	public String generateNewRefreshTokenFromOldToken(String token) {
		return buildToken(extractClaimsFromToken(token), "userRefreshToken", refreshTokenExpiration);
	}

	/**
	 * Extracts claims for creating a new token from an existing token.
	 *
	 * @param token The existing JWT token.
	 * @return A map of claims extracted from the token.
	 */
	private Map<String, Object> extractClaimsFromToken(String token) {
		Claims claims = extractAllClaims(token);
		Map<String, Object> extractedClaims = new HashMap<>();
		extractedClaims.put("name", claims.get("name"));
		extractedClaims.put("roles", claims.get("roles"));
		extractedClaims.put("email", claims.get("email"));
		extractedClaims.put("uuid", claims.get("uuid"));
		extractedClaims.put("tenant", claims.get("tenant"));
		return extractedClaims;
	}

	/**
	 * Creates a map of claims for a token.
	 *
	 * @param authUser The authenticated user.
	 * @param redisAuthUserId UUID stored in Redis.
	 * @return A map of claims.
	 */
	private Map<String, Object> createClaims(AuthUser authUser, String redisAuthUserId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("name", authUser.getFirstName() + " " + authUser.getLastName());
		claims.put("roles", authUser.getRoles());
		claims.put("email", authUser.getEmail());
		claims.put("uuid", redisAuthUserId);
		claims.put("tenant", TenantContext.getCurrentTenant());
		return claims;
	}

	/**
	 * Builds a token with the provided claims, subject, and expiration time.
	 *
	 * @param claims The claims to include in the token.
	 * @param subject The token's subject.
	 * @param expTime The expiration time in milliseconds.
	 * @return The generated token as a string.
	 */
	private String buildToken(Map<String, Object> claims, String subject, Long expTime) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expTime))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	/**
	 * Extracts the UUID stored in Redis from the token.
	 *
	 * @param token The JWT token.
	 * @return The extracted UUID.
	 */
	public String extractRedisAuthUserId(String token) {
		return extractClaim(token, claims -> (String) claims.get("uuid"));
	}
	
	public boolean validateToken(String token, OidcUser oidcUser) {
		try {
			// Get the public key
			PublicKey publicKey = getPublicKeyFromOidcProvider(token, oidcUser.getIssuer().toString());
			log.info("---Issuer OIDC -- " + oidcUser.getIssuer().toString());
			// Parse and validate the token using the public key
			Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();

			log.info("Token signature is valid");
			return true;
		} catch (Exception e) {
			log.error("Token validation failed: {}", e.getMessage());
			return false;
		}
	}

	public PublicKey getPublicKeyFromOidcProvider(String token, String issuer) throws Exception {
		// Discover the JWKS URI from the OIDC provider's discovery endpoint
		String discoveryUrl = issuer + "/.well-known/openid-configuration";
		Map<String, String> config = objectMapper.readValue(new URL(discoveryUrl), Map.class);
		String jwksUri = config.get("jwks_uri");
		if (Objects.isNull(jwksUri)) {
			throw new IllegalStateException("JWKS URI not found in the OIDC discovery document.");
		}

		// Fetch the JWKS (JSON Web Key Set) from the URI
		Map<String, Object> jwks = objectMapper.readValue(new URL(jwksUri), Map.class);
		List<Map<String, Object>> keys = (List<Map<String, Object>>) jwks.get("keys");

		// Find the public key by matching the 'kid' in the JWT header
		String kid = getKidFromToken(token);
		for (Map<String, Object> key : keys) {
			if (key.get("kid").equals(kid)) {
				// Parse the public key from the JWKS
				String modulus = (String) key.get("n");
				String exponent = (String) key.get("e");

				// Convert the modulus and exponent to RSAPublicKey
				return getRSAPublicKey(modulus, exponent);
			}
		}
		throw new IllegalStateException("Public key not found for kid: " + kid);
	}

	// Method to extract 'kid' from JWT header
	private String getKidFromToken(String token) {
		// Get JWT header and extract 'kid'
		try {
			String header = token.split("\\.")[0];
			byte[] decodedHeader = Base64.getDecoder().decode(header);
			Map<String, Object> headerMap = objectMapper.readValue(decodedHeader, Map.class);
			return (String) headerMap.get("kid");
		} catch (Exception e) {
			throw new JwtException("Error extracting 'kid' from token", e);
		}
	}

	private RSAPublicKey getRSAPublicKey(String modulus, String exponent) {
		try {
			// Decode modulus and exponent from base64 URL encoding
			byte[] modBytes = Base64.getUrlDecoder().decode(modulus);
			byte[] expBytes = Base64.getUrlDecoder().decode(exponent);

			// Create RSAPublicKey using the decoded modulus and exponent
			java.security.spec.RSAPublicKeySpec keySpec = new java.security.spec.RSAPublicKeySpec(
					new java.math.BigInteger(1, modBytes), new java.math.BigInteger(1, expBytes));
			java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new JwtException("Error creating RSAPublicKey from modulus and exponent", e);
		}
	}
}
