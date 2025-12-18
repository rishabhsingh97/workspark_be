package com.workspark.authservice.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.workspark.authservice.constant.Constants;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Value("${localhost.url}")
	private String localhost_url;

	@Value("${localhost.description}")
	private String localhost_description;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title(Constants.SWAGGER_TITLE).version(Constants.SWAGGER_VERSION)
						.description(Constants.SWAGGER_DESCRIPTION))
				.servers(List.of(new Server().url(localhost_url).description(localhost_description)));
	}

}
