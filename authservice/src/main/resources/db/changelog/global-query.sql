-- Global Oauth 2 table
CREATE TABLE IF NOT EXISTS `oauth2_registered_client` (
  `registration_id` varchar(100) NOT NULL,
  `client_id` varchar(100) NOT NULL,
  `client_secret` varchar(200) DEFAULT NULL,
  `client_authentication_method` varchar(100) NOT NULL,
  `authorization_grant_type` varchar(100) NOT NULL,
  `client_name` varchar(200) DEFAULT NULL,
  `redirect_uri` varchar(1000) NOT NULL,
  `scopes` varchar(1000) NOT NULL,
  `authorization_uri` varchar(1000) DEFAULT NULL,
  `token_uri` varchar(1000) NOT NULL,
  `jwk_set_uri` varchar(1000) DEFAULT NULL,
  `issuer_uri` varchar(1000) DEFAULT NULL,
  `user_info_uri` varchar(1000) DEFAULT NULL,
  `user_info_authentication_method` varchar(100) DEFAULT NULL,
  `user_name_attribute_name` varchar(100) DEFAULT NULL,
  `configuration_metadata` varchar(2000) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`registration_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT IGNORE INTO `oauth2_registered_client` VALUES ('mindfire1','NPnQ17CcbOgXqqbJwDNSykymSHmogagxpzAdSIoo','c0bgrRung3TeoO7lKXwBKwM3SeMivBjCZfOm7ds0n8GhkZJhhX2on4nYnaUh69oggFK7wWUKkBSiFpwd44VXql2lIrNYYwB4ndZ4bTG7RcmPKF2RoD9Fxidm2m4jBGhd','client_secret_basic','authorization_code','mindfire1','{baseUrl}/{action}/oauth2/code/{registrationId}','openid,email,profile','http://localhost:9000/application/o/authorize/','http://localhost:9000/application/o/token/','http://localhost:9000/application/o/mindfire1/jwks/','http://localhost:9000/application/o/mindfire1/','http://localhost:9000/application/o/userinfo/','header','sub','{\"@class\":\"java.util.Collections$UnmodifiableMap\"}',1,'2025-01-06 15:59:52','2025-01-09 11:27:56'),('mindfire2','uaWrgsQNcI2Xyuc4VAGApCIFmf3GiY7UUjXc6PyU','1KMoYpOfMzS2jJ9RgXLLoKyLua1Or3W8FhOFX5Qz340gog8iiuwa9Rq4yIzquF176jk4D7Bhk3ZcNVUPiIy1JRPlFuwJ2WjJJGXAnH9valMW1xLfpO67ltTdDanQ3HAE','client_secret_basic','authorization_code','mindfire2','http://localhost:8080/auth/login/oauth2/code/{registrationId}','openid,email,profile','http://localhost:9000/application/o/authorize/','http://localhost:9000/application/o/token/','http://localhost:9000/application/o/mindfire2/jwks/','http://localhost:9000/application/o/mindfire2/','http://localhost:9000/application/o/userinfo/','header','sub','{\"@class\":\"java.util.Collections$UnmodifiableMap\"}',1,'2025-01-06 15:59:52','2025-01-14 12:30:50');
