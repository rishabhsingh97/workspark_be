package com.workspark.authenticationservice.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.workspark.authenticationservice.client.UserServiceClient;
import com.workspark.authenticationservice.util.JwtUtil;
import com.workspark.commonconfig.service.RedisAuthUserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CustomOAuth2AuthenticationSuccessHandlerTest {

    @InjectMocks
    private CustomOAuth2AuthenticationSuccessHandler authenticationSuccessHandler;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private RedisAuthUserService redisAuthUserService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private OAuth2AuthenticationToken oauth2AuthenticationToken;

    @Mock
    private OidcUser oidcUser;

    private final String tenantName = "tenant";
    private final String email = "admin@gmail.com";
    private final String name = "Test Admin";
    private final String generatedToken = "generatedJwtToken";
    private final String accessToken = "validAccessToken";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test successful authentication with valid token.
     */
    @Test
    void testOnAuthenticationSuccessValidToken() throws IOException, ServletException {
        log.info("Running testOnAuthenticationSuccessValidToken");

        // Arrange
        when(request.getHeader("X-Tenant")).thenReturn(tenantName);
        when(oauth2AuthenticationToken.getPrincipal()).thenReturn(oidcUser);
        when(oidcUser.getAttribute("email")).thenReturn(email);
        when(oidcUser.getAttribute("name")).thenReturn(name);

        // Mock the getIdToken() method to return a mock OidcIdToken
        OidcIdToken mockedOidcIdToken = mock(OidcIdToken.class);
        when(oidcUser.getIdToken()).thenReturn(mockedOidcIdToken);

        // Simulate a valid token by returning a valid accessToken
        String validAccessToken = "validAccessToken";
        when(mockedOidcIdToken.getTokenValue()).thenReturn(validAccessToken);

        // Act
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, oauth2AuthenticationToken);

        log.info("testOnAuthenticationSuccessValidToken passed successfully.");
    }


    /**
     * Test failed authentication due to invalid token.
     */
    @Test
    void testOnAuthenticationSuccessInvalidToken() throws IOException, ServletException {
        log.info("Running testOnAuthenticationSuccessInvalidToken");

        // Arrange
        when(request.getHeader("X-Tenant")).thenReturn(tenantName);
        when(oauth2AuthenticationToken.getPrincipal()).thenReturn(oidcUser);
        when(oidcUser.getAttribute("email")).thenReturn(email);
        when(oidcUser.getAttribute("name")).thenReturn(name);

        // Mock the getIdToken() method to return a mock OidcIdToken
        OidcIdToken mockedOidcIdToken = mock(OidcIdToken.class);
        when(oidcUser.getIdToken()).thenReturn(mockedOidcIdToken);

        // Simulate invalid token by returning null or an empty value from getTokenValue() or invalid accessToken
        when(mockedOidcIdToken.getTokenValue()).thenReturn(null); // Simulating an invalid or null token.

        // Mock the token validation to return false
//        when(jwtUtil.validateToken(anyString(), eq(oidcUser))).thenReturn(false);

        // Act
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, oauth2AuthenticationToken);

        // Assert
        verify(response).sendRedirect("/error");
        verify(response, never()).addCookie(any(Cookie.class));
        verify(response, never()).sendRedirect("http://localhost:4200/auth/auto_login?token=" + generatedToken);
        log.info("testOnAuthenticationSuccessInvalidToken passed successfully.");
    }

}
