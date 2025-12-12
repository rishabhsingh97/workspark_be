package com.workspark.authenticationservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.workspark.authenticationservice.repository.CustomPersistedClientRegistrationRepo;
import com.workspark.authenticationservice.repository.Oauth2RegisteredClientRepository;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceApplicationTests {

    @Mock
    private Oauth2RegisteredClientRepository oauth2RegisteredClientRepository;

    @InjectMocks
    private CustomPersistedClientRegistrationRepo customPersistedClientRegistrationRepo;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Tests if the Spring application context loads successfully.
     * Verifies that the application context is not null, ensuring that all beans are correctly initialized.
     */
    @Test
    void contextLoads() {
        // Verify if the context is loaded successfully
        assertThat(applicationContext).isNotNull();
    }

    /**
     * Tests the configuration of the PasswordEncoder bean in the Spring context.
     * Verifies that the PasswordEncoder bean is correctly configured as an instance of BCryptPasswordEncoder.
     */
    @Test
    void testPasswordEncoderBean() {
        // Verify if PasswordEncoder bean is correctly configured
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    /**
     * Tests if the CustomPersistedClientRegistrationRepo bean is correctly initialized in the Spring context.
     * Verifies that the bean is not null and properly available in the application context.
     */
    @Test
    void testCustomPersistedClientRegistrationRepoBean() {
        // Verify if the CustomPersistedClientRegistrationRepo bean is properly initialized
        CustomPersistedClientRegistrationRepo customRepo = applicationContext.getBean(CustomPersistedClientRegistrationRepo.class);
        assertThat(customRepo).isNotNull();
    }
    
    /**
     * Verifies that the injected CustomPersistedClientRegistrationRepo instance is not null.
     * Ensures that the repository is properly initialized by Mockito and injected into the test class.
     */
    @Test
    void testCustomPersistedClientRegistrationRepo() {
        // Test if the repository was correctly initialized
        assertThat(customPersistedClientRegistrationRepo).isNotNull();
    }
    
    // Test Configuration to define necessary beans for testing
    @Configuration
    static class TestConfig {

        @Bean
        public CustomPersistedClientRegistrationRepo customPersistedClientRegistrationRepo(Oauth2RegisteredClientRepository oauth2RegisteredClientRepository) {
            return new CustomPersistedClientRegistrationRepo(oauth2RegisteredClientRepository);
        }

        @Bean
        public Oauth2RegisteredClientRepository oauth2RegisteredClientRepository() {
            return mock(Oauth2RegisteredClientRepository.class); // Mock the dependency
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(); // Provide a PasswordEncoder bean
        }
    }
}
	