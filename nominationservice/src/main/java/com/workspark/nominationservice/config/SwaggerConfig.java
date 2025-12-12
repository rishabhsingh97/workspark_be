package com.workspark.nominationservice.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Nomination API")
                        .version("1.0")
                        .description("API for managing nominations"))
                        .servers(List.of(
                        new Server().url("http://localhost:8080/nomination").description("Api Gateway - local")
                    ));

    }
}

























/*
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * springfox.documentation.builders.PathSelectors; import
 * springfox.documentation.builders.RequestHandlerSelectors; import
 * springfox.documentation.spi.DocumentationType; import
 * springfox.documentation.spring.web.plugins.Docket; import
 * springfox.documentation.swagger2.annotations.EnableSwagger2;
 * 
 * @Configuration
 * 
 * @EnableSwagger2 public class SwaggerConfig {
 * 
 * @Bean public Docket api() { return new
 * Docket(DocumentationType.SWAGGER_2)//.OAS_30 .select()
 * .apis(RequestHandlerSelectors.basePackage("com.workspark.controller"))
 * .paths(PathSelectors.any()) .build(); } }
 */

