package com.workspark.lib.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@ComponentScan(basePackages = {"com.workspark.lib"})
@EnableRedisRepositories(basePackages = "com.workspark.lib.repository")
public class LibraryConfig {

    @Configuration
    @EnableJpaRepositories(basePackages = "com.workspark.lib.repository")
    @ConditionalOnBean(name = "entityManagerFactory")
    static class JpaConfig { }
}
