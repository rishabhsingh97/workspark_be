package com.workspark.authenticationservice.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspark.authenticationservice.model.ChangePasswordRequest;
import com.workspark.authenticationservice.util.UtilityMethod;

@ExtendWith(MockitoExtension.class)  // Using Mockito Extension for potential mock setup (although it's not required here)
class UtilityMethodTest {

    // Setup any shared resources or variables here
    private ObjectMapper objectMapper;

    /**
     * Initializes the necessary components before each test.
     * This method is executed before each test case and sets up the ObjectMapper
     * which is used in assertions to compare JSON strings.
     */
    @BeforeEach
    void setUp() {
        // Initialize ObjectMapper to validate JSON output
        objectMapper = new ObjectMapper();
    }	
	/**
     * Test method for successful conversion of an object to JSON string.
     */
    @Test
    public void testAsJsonStringSuccess() throws Exception {
        // Arrange
        ChangePasswordRequest testObject = new ChangePasswordRequest("password", "pass","tenant");

        // Act
        String jsonString = UtilityMethod.asJsonString(testObject);

        // Assert
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJsonString = objectMapper.writeValueAsString(testObject);
        assertEquals(expectedJsonString, jsonString, "The JSON string should match the expected format.");
    }

    /**
     * Test method for handling exception when the object cannot be converted to JSON.
     */
    @Test
    public void testAsJsonStringFailure() {
        // Arrange
        Object invalidObject = new Object() {
            // This class does not have standard properties and may cause issues during JSON serialization.
        };

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            UtilityMethod.asJsonString(invalidObject);
        }, "Expected a RuntimeException when object conversion fails");
    }

}
