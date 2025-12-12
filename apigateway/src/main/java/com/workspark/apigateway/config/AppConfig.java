package com.workspark.apigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private Set<String> whitelistedPaths;
}
