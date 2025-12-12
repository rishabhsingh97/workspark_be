package com.workspark.authenticationservice.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

//    @InjectMocks
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Mock
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private FilterChain filterChain;
//
//    @Mock
//    private Authentication authentication;
//
//    @Mock
//    private SecurityContext securityContext;
//
//    @BeforeEach
//    void setUp() {
//        // Mock the SecurityContext and set it in the SecurityContextHolder
//        securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//    }
//
//    @Test
//    void testDoFilterInternalValidToken() throws ServletException, IOException {
//        // Arrange
//        String token = "valid.jwt.token";
//        when(jwtTokenProvider.resolveToken(request)).thenReturn(token);
//        when(jwtTokenProvider.validateToken(token)).thenReturn(true);
//        when(jwtTokenProvider.getAuthentication(token)).thenReturn(authentication);
//
//        // Act
//        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
//
//        // Assert: Check that the security context's authentication was set correctly
//        verify(jwtTokenProvider, times(1)).resolveToken(request);
//        verify(jwtTokenProvider, times(1)).validateToken(token);
//        verify(jwtTokenProvider, times(1)).getAuthentication(token);
//        verify(securityContext, times(1)).setAuthentication(authentication);
//        
//        // Ensure the filter chain is continued
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
//
//    @Test
//    void testDoFilterInternalInvalidToken() throws ServletException, IOException {
//        // Arrange
//        String token = "invalid.jwt.token";
//        when(jwtTokenProvider.resolveToken(request)).thenReturn(token);
//        when(jwtTokenProvider.validateToken(token)).thenReturn(false);
//
//        // Act
//        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
//
//        // Assert: Check that the authentication is not set in the security context
//        verify(jwtTokenProvider, times(1)).resolveToken(request);
//        verify(jwtTokenProvider, times(1)).validateToken(token);
//        verify(jwtTokenProvider, never()).getAuthentication(token);
//        verify(securityContext, never()).setAuthentication(any(Authentication.class));
//
//        // Ensure the filter chain is continued
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
//
//    @Test
//    void testDoFilterInternalNoToken() throws ServletException, IOException {
//        // Arrange: No token provided
//        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);
//
//        // Act
//        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
//
//        // Assert: Check that no further action is taken, and filter chain is continued
//        verify(jwtTokenProvider, times(1)).resolveToken(request);
//        verify(jwtTokenProvider, never()).validateToken(anyString());
//        verify(jwtTokenProvider, never()).getAuthentication(anyString());
//        verify(securityContext, never()).setAuthentication(any(Authentication.class));
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
}
