package com.workspark.authenticationservice.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oauth2_registered_client")
@Getter
@Setter
@NoArgsConstructor
public class Oauth2RegisteredClientEntity implements Serializable{

	private static final long serialVersionUID = -4809090786153235379L;

	@Id
	@Column(name = "registration_id", length = 100, nullable = false)
	private String registrationId;

	@Column(name = "client_id", length = 100, nullable = false)
	private String clientId;

	@Column(name = "client_secret", length = 200)
	private String clientSecret;

	@Column(name = "client_authentication_method", length = 100, nullable = false)
	private String clientAuthenticationMethod;

	@Column(name = "authorization_grant_type", length = 100, nullable = false)
	private String authorizationGrantType;

	@Column(name = "client_name", length = 200)
	private String clientName;

	@Column(name = "redirect_uri", length = 1000, nullable = false)
	private String redirectUri;

	@Column(name = "scopes", length = 1000, nullable = false)
	private String scopes;

	@Column(name = "authorization_uri", length = 1000)
	private String authorizationUri;

	@Column(name = "token_uri", length = 1000, nullable = false)
	private String tokenUri;

	@Column(name = "jwk_set_uri", length = 1000)
	private String jwkSetUri;

	@Column(name = "issuer_uri", length = 1000)
	private String issuerUri;

	@Column(name = "user_info_uri", length = 1000)
	private String userInfoUri;

	@Column(name = "user_info_authentication_method", length = 100)
	private String userInfoAuthenticationMethod;

	@Column(name = "user_name_attribute_name", length = 100)
	private String userNameAttributeName;

	@Column(name = "configuration_metadata", length = 2000)
	private String configurationMetadata;
}