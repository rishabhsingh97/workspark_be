package com.workspark.certificateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class CertificateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertificateServiceApplication.class, args);
	}

}
