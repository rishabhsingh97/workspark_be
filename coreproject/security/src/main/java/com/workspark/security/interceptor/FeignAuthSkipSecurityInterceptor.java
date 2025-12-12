package com.workspark.security.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Feign interceptor to skip authentication for internal services.
 * This interceptor adds a custom header 'X-InternalServiceSkipAuth' with value 'true' to the request.
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "workspark.feign", havingValue = "true", matchIfMissing = true)
public class FeignAuthSkipSecurityInterceptor implements RequestInterceptor {

    /**
     * Applies the custom header to the request template.
     * @param template RequestTemplate to which the header is added.
     */
    @Override
    public void apply(RequestTemplate template) {
        template.header("X-InternalServiceSkipAuth", "true");
    }

}
