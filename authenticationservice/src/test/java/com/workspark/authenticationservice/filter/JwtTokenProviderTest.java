package com.workspark.authenticationservice.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)  // This is the key annotation for Mockito with JUnit 5
public class JwtTokenProviderTest {

//    private static final String SECRET_KEY = "secret";
//    private static final String TEST_TOKEN = "valid.jwt.token";
//    private static final String USERNAME = "user";
//
//    @InjectMocks
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @BeforeEach
//    void setUp() {
//        // Manually set secretKey and validityInMilliseconds values to avoid using @Value annotation in tests
//        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
//        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L); // long value for milliseconds
//    }
//
//    @Test
//    void testResolveToken() {
//        // Arrange
//        String bearerToken = "Bearer " + TEST_TOKEN;
//        when(request.getHeader("Authorization")).thenReturn(bearerToken);
//
//        // Act
//        String token = jwtTokenProvider.resolveToken(request);
//
//        // Assert
//        assertNotNull(token);
//        assertEquals(TEST_TOKEN, token);
//    }
//
//    @Test
//    void testResolveTokenWithoutBearer() {
//        // Arrange
//        when(request.getHeader("Authorization")).thenReturn(TEST_TOKEN);
//
//        // Act
//        String token = jwtTokenProvider.resolveToken(request);
//
//        // Assert
//        assertNull(token);
//    }
//
//    @Test
//    void testValidateTokenValid() {
//        // Arrange
//        String validToken = generateValidToken();
//
//        // Act
//        boolean isValid = jwtTokenProvider.validateToken(validToken);
//
//        // Assert
//        assertTrue(isValid);
//    }
//
//    @Test
//    void testValidateTokenInvalid() {
//        // Arrange
//        String invalidToken = "invalid.token";
//
//        // Act
//        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
//
//        // Assert
//        assertFalse(isValid);
//    }
//
//    @Test
//    void testGetAuthentication() {
//        // Arrange
//        String validToken = generateValidToken();
//        
//        // Act
//        Authentication authentication = jwtTokenProvider.getAuthentication(validToken);
//
//        // Assert
//        assertNotNull(authentication);
//        assertEquals(USERNAME, authentication.getName());
//        assertEquals(validToken, authentication.getCredentials());
//    }
//
//    @Test
//    void testGetAuthenticationInvalidToken() {
//        // Arrange
//        String invalidToken = "invalid.token";
//        
//        // Act and Assert
//        assertThrows(JwtException.class, () -> jwtTokenProvider.getAuthentication(invalidToken));
//    }
//
//    // Utility method to generate a valid JWT token (assuming it's a simple token generation with username as subject)
//    private String generateValidToken() {
//        return Jwts.builder()
//                .setSubject(USERNAME)
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
}
