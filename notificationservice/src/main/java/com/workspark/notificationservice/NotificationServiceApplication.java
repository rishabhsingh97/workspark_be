package com.workspark.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class NotificationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@Bean
	ITemplateResolver stringTemplateResolver() {
		StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
		stringTemplateResolver.setTemplateMode("HTML");
		stringTemplateResolver.setCacheable(false);
		return stringTemplateResolver;
	}
}
