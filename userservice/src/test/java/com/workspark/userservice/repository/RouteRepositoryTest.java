package com.workspark.userservice.repository;

import com.workspark.models.enums.UserRole;
import com.workspark.userservice.model.entitity.Role;
import com.workspark.userservice.model.entitity.Route;
import com.workspark.userservice.repo.RouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/workspark_test",
        "spring.datasource.username=root",
        "spring.datasource.password=root",
        "spring.liquibase.drop-first=true"
})
class RouteRepositoryTest {

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void save_ShouldCreateRoute() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(UserRole.SUPER_ADMIN);
        role.setRoleDesc("Super Admin Role");

        Route route = new Route();
        route.setPath("/api/v1/auth");
        route.setTitle("TEST ROUTE");
        route.setRoles(List.of(role));

        // Act
        Route savedRoute = routeRepository.save(route);

        // Assert
        assertNotNull(savedRoute.getId());
        assertEquals(route.getTitle(), savedRoute.getTitle());
    }
}
