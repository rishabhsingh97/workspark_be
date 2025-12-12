package com.workspark.certificateservice.config;

import com.workspark.models.response.BaseRes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String SCHEME_NAME = "Bearer Authentication";
    private static final String SCHEME = "bearer";
    private static final String LOCALHOST_API_GATEWAY_URL = "http://localhost:8080";
    private static final String LOCALHOST_API_GATEWAY_DESCRIPTION = "Api Gateway - Local";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Certificate Service API")
                        .version("1.0")
                        .description("API documentation for the Workspark Certificate Service"))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(SCHEME)
                                .bearerFormat("JWT"))
                        .addResponses("default", new ApiResponse()
                                .description("Default Response")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>().$ref("#/components/schemas/BaseRes"))))))
                        .servers(List.of(
                                new Server()
                                        .url(LOCALHOST_API_GATEWAY_URL)
                                        .description(LOCALHOST_API_GATEWAY_DESCRIPTION)
                        ));
    }
}
