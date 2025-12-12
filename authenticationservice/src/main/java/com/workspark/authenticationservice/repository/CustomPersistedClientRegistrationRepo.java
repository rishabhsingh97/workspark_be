package com.workspark.authenticationservice.repository;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.workspark.authenticationservice.projections.Oauth2RegisteredClientProjection;

public class CustomPersistedClientRegistrationRepo implements ClientRegistrationRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomPersistedClientRegistrationRepo.class);

	private static final Map<String, AuthorizationGrantType> GRANT_TYPE_MAP = Map.of(
			AuthorizationGrantType.AUTHORIZATION_CODE.getValue(), AuthorizationGrantType.AUTHORIZATION_CODE,
			AuthorizationGrantType.CLIENT_CREDENTIALS.getValue(), AuthorizationGrantType.CLIENT_CREDENTIALS,
			AuthorizationGrantType.JWT_BEARER.getValue(), AuthorizationGrantType.JWT_BEARER,
			AuthorizationGrantType.DEVICE_CODE.getValue(), AuthorizationGrantType.DEVICE_CODE,
			AuthorizationGrantType.REFRESH_TOKEN.getValue(), AuthorizationGrantType.REFRESH_TOKEN);

	private static final Map<String, ClientAuthenticationMethod> CLIENT_AUTH_METHOD_MAP = Map.of(
			ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue(), ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
			ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue(), ClientAuthenticationMethod.CLIENT_SECRET_POST,
			ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue(), ClientAuthenticationMethod.CLIENT_SECRET_JWT,
			ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue(), ClientAuthenticationMethod.PRIVATE_KEY_JWT,
			ClientAuthenticationMethod.NONE.getValue(), ClientAuthenticationMethod.NONE);

	private static final Map<String, AuthenticationMethod> USER_INFO_AUTH_METHOD_MAP = Map.of(
			AuthenticationMethod.FORM.getValue(), AuthenticationMethod.FORM, AuthenticationMethod.HEADER.getValue(),
			AuthenticationMethod.HEADER, AuthenticationMethod.QUERY.getValue(), AuthenticationMethod.QUERY);

	private final Oauth2RegisteredClientRepository repository;

	/**
	 * Constructor to initialize JdbcClientRegRep with the provided repository.
	 * 
	 * @param repository the repository to fetch OAuth2 client details.
	 * @throws NullPointerException if repository is null.
	 */
	public CustomPersistedClientRegistrationRepo(Oauth2RegisteredClientRepository repository) {
		this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
		LOGGER.info("JdbcClientRegRep initialized with the provided repository.");
	}

	/**
	 * Finds a client registration by its registration ID.
	 * 
	 * @param registrationId the registration ID to search for.
	 * @return the ClientRegistration corresponding to the given registrationId.
	 * @throws IllegalArgumentException if no client is found with the given
	 *                                  registrationId.
	 */
	@Override
	public ClientRegistration findByRegistrationId(String registrationId) {
		LOGGER.debug("Searching for client registration with ID: {}", registrationId);

		Assert.hasText(registrationId, "registrationId cannot be empty");

		return repository.findByRegistrationId(registrationId).map(oauthConfigDetails -> {
			LOGGER.debug("Client registration found for ID: {}", registrationId);
			return createClientRegistration(oauthConfigDetails);
		}).orElseThrow(() -> {
			return new IllegalArgumentException("No client found with registrationId: " + registrationId);
		});
	}

	/**
	 * Converts an {@link Oauth2RegisteredClientProjection} to a
	 * {@link ClientRegistration}.
	 * 
	 * @param c the Oauth2RegisteredClientProjection object to convert.
	 * @return the corresponding ClientRegistration.
	 */
	private static ClientRegistration createClientRegistration(Oauth2RegisteredClientProjection c) {
		LOGGER.debug("Creating ClientRegistration for registration ID: {}", c.getRegistrationId());

		return ClientRegistration.withRegistrationId(c.getRegistrationId()).clientId(c.getClientId())
				.clientSecret(c.getClientSecret())
				.clientAuthenticationMethod(resolveClientAuthenticationMethod(c.getClientAuthenticationMethod()))
				.authorizationGrantType(resolveAuthorizationGrantType(c.getAuthorizationGrantType()))
				.clientName(c.getClientName()).redirectUri(c.getRedirectUri()).scope(parseScopes(c.getScopes()))
				.authorizationUri(c.getAuthorizationUri()).tokenUri(c.getTokenUri()).jwkSetUri(c.getJwkSetUri())
				.issuerUri(c.getIssuerUri()).userInfoUri(c.getUserInfoUri())
				.userInfoAuthenticationMethod(resolveUserInfoAuthenticationMethod(c.getClientAuthenticationMethod()))
				.userNameAttributeName(c.getUserNameAttributeName()).build();
	}

	/**
	 * Parses a comma-separated list of scopes and returns a Set of scopes.
	 * 
	 * @param scopes the comma-separated list of scopes.
	 * @return a Set containing the scopes.
	 */
	private static Set<String> parseScopes(String scopes) {
		LOGGER.debug("Parsing scopes: {}", scopes);

		if (StringUtils.hasText(scopes)) {
			return Set.copyOf(StringUtils.commaDelimitedListToSet(scopes));
		}
		return Set.of();
	}

	/**
	 * Resolves the AuthorizationGrantType based on the given string.
	 * 
	 * @param authorizationGrantType the string representing the authorization grant
	 *                               type.
	 * @return the corresponding AuthorizationGrantType.
	 */
	private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
		LOGGER.debug("Resolving AuthorizationGrantType for: {}", authorizationGrantType);

		if (StringUtils.hasText(authorizationGrantType)) {
			return GRANT_TYPE_MAP.getOrDefault(authorizationGrantType,
					new AuthorizationGrantType(authorizationGrantType));
		}
		return AuthorizationGrantType.AUTHORIZATION_CODE;
	}

	/**
	 * Resolves the ClientAuthenticationMethod based on the given string.
	 * 
	 * @param clientAuthenticationMethod the string representing the client
	 *                                   authentication method.
	 * @return the corresponding ClientAuthenticationMethod.
	 */
	private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
		LOGGER.debug("Resolving ClientAuthenticationMethod for: {}", clientAuthenticationMethod);

		if (StringUtils.hasText(clientAuthenticationMethod)) {
			return CLIENT_AUTH_METHOD_MAP.getOrDefault(clientAuthenticationMethod,
					new ClientAuthenticationMethod(clientAuthenticationMethod));
		}
		return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
	}

	/**
	 * Resolves the AuthenticationMethod for user info based on the given string.
	 * 
	 * @param userInfoAuthenticationMethod the string representing the user info
	 *                                     authentication method.
	 * @return the corresponding AuthenticationMethod.
	 */
	private static AuthenticationMethod resolveUserInfoAuthenticationMethod(String userInfoAuthenticationMethod) {
		LOGGER.debug("Resolving UserInfoAuthenticationMethod for: {}", userInfoAuthenticationMethod);

		if (StringUtils.hasText(userInfoAuthenticationMethod)) {
			return USER_INFO_AUTH_METHOD_MAP.getOrDefault(userInfoAuthenticationMethod,
					new AuthenticationMethod(userInfoAuthenticationMethod));
		}
		return AuthenticationMethod.FORM;
	}
}