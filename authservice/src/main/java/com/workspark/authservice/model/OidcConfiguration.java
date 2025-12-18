package com.workspark.authservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OidcConfiguration {

    private String issuer;
    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;
    private String tokenEndpoint;
    private String userinfoEndpoint;
    private String endSessionEndpoint;
    private String introspectionEndpoint;
    private String revocationEndpoint;
    private String deviceAuthorizationEndpoint;
    private List<String> responseTypesSupported;
    private List<String> responseModesSupported;
    private String jwksUri;
    private List<String> grantTypesSupported;
    private List<String> idTokenSigningAlgValuesSupported;
    private List<String> subjectTypesSupported;
    private List<String> tokenEndpointAuthMethodsSupported;
    private List<String> acrValuesSupported;
    private List<String> scopesSupported;
    private boolean requestParameterSupported;
    private List<String> claimsSupported;
    private boolean claimsParameterSupported;
    private List<String> codeChallengeMethodsSupported;
    
}