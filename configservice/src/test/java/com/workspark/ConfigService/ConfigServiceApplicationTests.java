package com.workspark.ConfigService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ConfigServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	@MockBean
	private ApplicationContext applicationContext; // Mock the application context if needed

	@Test
	void testMainMethod() {
		assertDoesNotThrow(() -> ConfigServiceApplication.main(new String[]{}), "Main method should run without exceptions");
	}

}
