package com.workspark.authenticationservice.projections;

public interface Oauth2RegisteredClientProjection {

	String getRegistrationId();
    String getClientId();
    String getClientSecret();
    String getClientAuthenticationMethod();
    String getAuthorizationGrantType();
    String getClientName();
    String getRedirectUri();
    String getScopes();
    String getAuthorizationUri();
    String getTokenUri();
    String getJwkSetUri();
    String getIssuerUri();
    String getUserInfoUri();
    String getUserInfoAuthenticationMethod();
    String getUserNameAttributeName();
    String getConfigurationMetadata();
}
