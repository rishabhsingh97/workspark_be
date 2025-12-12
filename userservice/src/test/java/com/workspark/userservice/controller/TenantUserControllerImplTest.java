package com.workspark.userservice.controller;

import com.workspark.models.enums.UserRole;
import com.workspark.models.request.SignupRequest;
import com.workspark.userservice.controller.controllerImpl.TenantUserControllerImpl;
import com.workspark.userservice.service.TenantUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TenantUserControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TenantUserService tenantUserService;

    @InjectMocks
    private TenantUserControllerImpl tenantUserController;

    @BeforeEach
    void setUp() {
        // Set up MockMvc with the standalone controller setup
        mockMvc = MockMvcBuilders.standaloneSetup(tenantUserController).build();
    }

    @Test
    void addTenantUser_ShouldReturnSuccess_WhenValidDataIsProvided() throws Exception {
        // Test if the user is successfully created when valid data is provided
        SignupRequest signupRequest = SignupRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("testuser@example.com")
                .phoneNumber("+1234567890")
                .password("password123")
                .roles(List.of(UserRole.TENANT_ADMIN, UserRole.USER))
                .tenant("tenantName")
                .build();

        mockMvc.perform(post("/api/v1/tenant-user")
                        .contentType("application/json")
                        .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"testuser@example.com\", \"phoneNumber\": \"+1234567890\", \"password\": \"password123\", \"roles\": [\"TENANT_ADMIN\", \"USER\"], \"tenant\": \"tenantName\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Signup successful"));

        // Verify the service method was called
        verify(tenantUserService, times(1)).addTenantUser(any(SignupRequest.class));
    }

//    @Test
//    void addTenantUser_MissingFirstName_ShouldReturnBadRequest() throws Exception {
//        // Creating a request with missing firstName
//        String requestBody = "{\"lastName\": \"Doe\", \"email\": \"testuser@example.com\", \"phoneNumber\": \"+1234567890\", \"password\": \"password123\", \"roles\": [\"TENANT_ADMIN\", \"USER\"], \"tenant\": \"tenantName\"}";
//
//        // Perform the request and simulate a validation error
//        MvcResult result = mockMvc.perform(post("/api/v1/tenant-user")
//                        .contentType("application/json")
//                        .content(requestBody))
//                .andExpect(status().isBadRequest())  // Expecting a bad request due to missing firstName
//                .andReturn();  // Capture the result to extract response details
//
//        // Manually assert the response body
//        String responseBody = result.getResponse().getContentAsString();
//        assertTrue(responseBody.contains("firstName: First name is required"));
//        assertTrue(responseBody.contains("\"success\": false"));
//        assertTrue(responseBody.contains("\"error\": \"Validation Error\""));
//        assertTrue(responseBody.contains("\"item\": null"));
//    }
//
//
//
//
//
//    @Test
//    void addTenantUser_InvalidEmail_ShouldReturnBadRequest() throws Exception {
//        // Creating a request with invalid email
//        String requestContent = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"invalid-email\", " +
//                "\"phoneNumber\": \"+1234567890\", \"password\": \"password123\", " +
//                "\"roles\": [\"TENANT_ADMIN\"] }";
//
//        mockMvc.perform(post("/api/v1/tenant-user")
//                        .contentType("application/json")
//                        .content(requestContent))
//                .andExpect(status().isBadRequest())  // Expecting a bad request due to invalid email format
//                .andExpect(content().string("Invalid email format"));  // Expecting validation message
//
//        // Verify the service method was not called
//        verify(tenantUserService, times(0)).addTenantUser(any(SignupRequest.class));
//    }
//
//    @Test
//    void addTenantUser_MissingRole_ShouldReturnBadRequest() throws Exception {
//        // Creating a request with no roles field
//        String requestContent = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"johndoe@example.com\", " +
//                "\"phoneNumber\": \"+1234567890\", \"password\": \"password123\" }";
//
//        mockMvc.perform(post("/api/v1/tenant-user")
//                        .contentType("application/json")
//                        .content(requestContent))
//                .andExpect(status().isBadRequest())  // Expecting a bad request due to missing roles
//                .andExpect(content().string("Roles are required"));  // Expecting validation message for missing roles
//
//        // Verify the service method was not called
//        verify(tenantUserService, times(0)).addTenantUser(any(SignupRequest.class));
//    }
//
//    @Test
//    void addTenantUser_UnauthorizedAccess_ShouldReturnForbidden() throws Exception {
//        // Mocking a user with a role that doesn't have access
//        String requestContent = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"johndoe@example.com\", " +
//                "\"phoneNumber\": \"+1234567890\", \"password\": \"password123\", " +
//                "\"roles\": [\"USER\"] }"; // User role instead of TENANT_ADMIN
//
//        // Mocking unauthorized access
//        mockMvc.perform(post("/api/v1/tenant-user")
//                        .contentType("application/json")
//                        .content(requestContent))
//                .andExpect(status().isForbidden())  // Expecting Forbidden error
//                .andExpect(content().string("Access Denied"));
//
//        // Verify the service method was not called
//        verify(tenantUserService, times(0)).addTenantUser(any(SignupRequest.class));
//    }


}
