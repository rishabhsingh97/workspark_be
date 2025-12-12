package com.workspark.userservice.repository;

import com.workspark.models.enums.UserRole;
import com.workspark.userservice.model.entitity.Role;
import com.workspark.userservice.repo.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/workspark_test",
        "spring.datasource.username=root",
        "spring.datasource.password=root",
        "spring.liquibase.drop-first=true"
})
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testGet_GetRoleById() {

        // Act
        Optional<Role> savedRole = roleRepository.findById(1L);

        // Assert
        assertNotNull(savedRole, "Saved role should have an ID");
    }

    @Test
    void testGet_GetRoleByRoleName() {

        // Act
        List<Role> savedRoles = roleRepository.findMasterUserRolesByRoleNameIn(
                List.of(UserRole.ADMIN,UserRole.USER)
        );

        // Assert
        assertNotNull(savedRoles, "Saved role should have an ID");
        assertEquals(2, savedRoles.size(), "Saved role should have an ID");
    }
}
