package com.workspark.userservice.service;

import com.workspark.models.enums.UserRole;
import com.workspark.models.pojo.AuthUser;
import com.workspark.models.response.PaginatedRes;
import com.workspark.userservice.exceptions.customExceptions.RouteException;
import com.workspark.userservice.model.dto.RouteDto;
import com.workspark.userservice.model.entitity.Route;
import com.workspark.userservice.model.entitity.Role;
import com.workspark.userservice.model.enums.ErrorMessages;
import com.workspark.userservice.repo.RouteRepository;
import com.workspark.userservice.repo.RoleRepository;
import com.workspark.userservice.service.serviceImpl.RouteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteServiceImplTest {

    @Mock
    private RouteRepository routeDataRepository;

    @Mock
    private RoleRepository masterUserRolesRepository;

    @InjectMocks
    private RouteServiceImpl routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoutes() {
        int page = 1;
        int size = 10;

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        AuthUser authUser = mock(AuthUser.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authUser);
        when(authUser.getRoles()).thenReturn(List.of(UserRole.ADMIN, UserRole.USER));  // Mocking roles

        List<Role> roles = List.of(
                Role.builder().roleName(UserRole.ADMIN).build(),
                Role.builder().roleName(UserRole.USER).build()
        );
        when(masterUserRolesRepository.findMasterUserRolesByRoleNameIn(anyList())).thenReturn(roles);

        // Creating Route mock data
        Route route1 = new Route();
        route1.setId(1L);
        route1.setPath("/path1");
        route1.setTitle("Route 1");

        Route route2 = new Route();
        route2.setId(2L);
        route2.setPath("/path2");
        route2.setTitle("Route 2");

        // Creating Page mock
        Page<Route> mockPage = new PageImpl<>(Arrays.asList(route1, route2));
        when(routeDataRepository.findRoutesByRolesIn(eq(roles), any(PageRequest.class))).thenReturn(mockPage);

        // Calling the method under test
        PaginatedRes<RouteDto> result = routeService.getAllRoutes(page, size);

        // Verifying the result
        assertNotNull(result);
        assertEquals(2, result.getData().size());  // Verify the number of routes
        assertEquals(1, result.getPageNo());  // Verify page number
        assertEquals(10, result.getPageSize());  // Verify page size
        assertEquals(2, result.getTotalCount());  // Verify total count

        // Verifying the mock interactions
        verify(routeDataRepository, times(1)).findRoutesByRolesIn(eq(roles), any(PageRequest.class));
    }


    @Test
    void testCreateRoute_Success() {
        RouteDto routeDto = new RouteDto();
        routeDto.setPath("/test");
        routeDto.setRoles(List.of(UserRole.ADMIN));

        Role role = new Role();
        role.setRoleName(UserRole.ADMIN);

        when(masterUserRolesRepository.findMasterUserRolesByRoleNameIn(routeDto.getRoles()))
                .thenReturn(List.of(role));
        when(routeDataRepository.existsByPath(routeDto.getPath())).thenReturn(false);

        Route route = new Route();
        route.setId(1L);
        when(routeDataRepository.save(any(Route.class))).thenReturn(route);

        RouteDto result = routeService.createRoute(routeDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(routeDataRepository, times(1)).save(any(Route.class));
        verify(masterUserRolesRepository, times(1)).findMasterUserRolesByRoleNameIn(routeDto.getRoles());
    }

    @Test
    void testCreateRoute_PathAlreadyExists() {
        RouteDto routeDto = new RouteDto();
        routeDto.setPath("/test");

        when(routeDataRepository.existsByPath(routeDto.getPath())).thenReturn(true);

        RouteException exception = assertThrows(RouteException.class, () -> routeService.createRoute(routeDto));

        assertEquals(ErrorMessages.ROUTE_ALREADY_EXISTS.arguments(routeDto.getPath()), exception.getMessage());

        verify(routeDataRepository, never()).save(any(Route.class));
    }

    @Test
    void testUpdateRoute_Success() {
        Long routeId = 1L;
        RouteDto routeDto = new RouteDto();
        routeDto.setPath("/new-path");
        routeDto.setRoles(List.of(UserRole.ADMIN));

        Role role = new Role();
        role.setRoleName(UserRole.ADMIN);

        Route route = new Route();
        route.setId(routeId);
        route.setPath("/old-path");

        when(routeDataRepository.findById(routeId)).thenReturn(Optional.of(route));
        when(masterUserRolesRepository.findMasterUserRolesByRoleNameIn(routeDto.getRoles()))
                .thenReturn(List.of(role));
        when(routeDataRepository.save(route)).thenReturn(route);

        RouteDto result = routeService.updateRoute(routeId, routeDto);

        assertNotNull(result);
        assertEquals(routeDto.getPath(), result.getPath());

        verify(routeDataRepository, times(1)).save(route);
    }

    @Test
    void testDeleteRoute_Success() {
        Long routeId = 1L;

        Route route = new Route();
        route.setId(routeId);

        when(routeDataRepository.findById(routeId)).thenReturn(Optional.of(route));
        doNothing().when(routeDataRepository).deleteById(routeId);

        assertDoesNotThrow(() -> routeService.deleteRoute(routeId));

        verify(routeDataRepository, times(1)).deleteById(routeId);
    }

    @Test
    void testGetRouteById_Success() {
        Long routeId = 1L;

        Route route = new Route();
        route.setId(routeId);

        when(routeDataRepository.findById(routeId)).thenReturn(Optional.of(route));

        RouteDto result = routeService.getRouteById(routeId);

        assertNotNull(result);
        assertEquals(routeId, result.getId());

        verify(routeDataRepository, times(1)).findById(routeId);
    }

    @Test
    void testGetRouteById_NotFound() {
        Long routeId = 1L;

        when(routeDataRepository.findById(routeId)).thenReturn(Optional.empty());

        RouteException exception = assertThrows(RouteException.class, () -> routeService.getRouteById(routeId));

        assertEquals(ErrorMessages.ROUTE_NOT_FOUND.arguments(routeId), exception.getMessage());
    }
}
