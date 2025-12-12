package com.workspark.authenticationservice.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import com.workspark.authenticationservice.projections.Oauth2RegisteredClientProjection;
import com.workspark.authenticationservice.repository.CustomPersistedClientRegistrationRepo;
import com.workspark.authenticationservice.repository.Oauth2RegisteredClientRepository;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to enable automatic mock injection
class CustomPersistedClientRegistrationRepoTest {

	@Mock
	private Oauth2RegisteredClientRepository repository;

	@InjectMocks
	private CustomPersistedClientRegistrationRepo customPersistedClientRegistrationRepo;

	@Mock
	private Oauth2RegisteredClientProjection oauth2RegisteredClientProjection;

	@BeforeEach
	void setUp() {
		// Mocking the behavior of the Oauth2RegisteredClientProjection class
		when(oauth2RegisteredClientProjection.getRegistrationId()).thenReturn("client123");
		when(oauth2RegisteredClientProjection.getClientId()).thenReturn("client_id");
		when(oauth2RegisteredClientProjection.getClientSecret()).thenReturn("client_secret");
		when(oauth2RegisteredClientProjection.getClientAuthenticationMethod()).thenReturn("client_secret_basic");
		when(oauth2RegisteredClientProjection.getAuthorizationGrantType()).thenReturn("authorization_code");
		when(oauth2RegisteredClientProjection.getClientName()).thenReturn("Client Name");
		when(oauth2RegisteredClientProjection.getRedirectUri()).thenReturn("http://redirect.uri");
		when(oauth2RegisteredClientProjection.getScopes()).thenReturn("read,write");
		when(oauth2RegisteredClientProjection.getAuthorizationUri()).thenReturn("http://auth.uri");
		when(oauth2RegisteredClientProjection.getTokenUri()).thenReturn("http://token.uri");
		when(oauth2RegisteredClientProjection.getJwkSetUri()).thenReturn("http://jwk.set.uri");
		when(oauth2RegisteredClientProjection.getIssuerUri()).thenReturn("http://issuer.uri");
		when(oauth2RegisteredClientProjection.getUserInfoUri()).thenReturn("http://userinfo.uri");
		when(oauth2RegisteredClientProjection.getUserNameAttributeName()).thenReturn("username");
	}

	/**
	 * Tests the 'findByRegistrationId' method of CustomPersistedClientRegistrationRepo.
	 * Verifies that the method correctly retrieves and returns a ClientRegistration 
	 * with the expected values when given a valid registration ID.
	 */
	@Test
	void testFindByRegistrationId() {
		// Arrange
		when(repository.findByRegistrationId("client123")).thenReturn(Optional.of(oauth2RegisteredClientProjection));

		// Act
		ClientRegistration clientRegistration = customPersistedClientRegistrationRepo.findByRegistrationId("client123");

		// Assert
		assertNotNull(clientRegistration);
		assertEquals("client123", clientRegistration.getRegistrationId());
		assertEquals("client_id", clientRegistration.getClientId());
		assertEquals("client_secret", clientRegistration.getClientSecret());
		assertEquals(AuthorizationGrantType.AUTHORIZATION_CODE, clientRegistration.getAuthorizationGrantType());
		assertEquals(ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
				clientRegistration.getClientAuthenticationMethod());
	}

}
