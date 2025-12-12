package com.workspark.userservice.controller;

import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import com.workspark.userservice.controller.controllerImpl.RouteControllerImpl;
import com.workspark.userservice.model.dto.RouteDto;
import com.workspark.userservice.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RouteControllerImplTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteControllerImpl routeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoutes() {
        int page = 1;
        int size = 10;

        PaginatedRes<RouteDto> mockResponse = new PaginatedRes<>();
        mockResponse.setPageSize(size);
        mockResponse.setPageNo(1);
        mockResponse.setTotalPages(1);
        mockResponse.setTotalCount(2);
        mockResponse.setData(Arrays.asList(new RouteDto(), new RouteDto()));

        when(routeService.getAllRoutes(page, size)).thenReturn(mockResponse);

        ResponseEntity<BaseRes<PaginatedRes<RouteDto>>> response = routeController.getAllRoutes(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Routes fetched successfully", response.getBody().getMessage());
        assertEquals(2, response.getBody().getItem().getData().size());

        verify(routeService, times(1)).getAllRoutes(page, size);
    }

    @Test
    void testGetRouteById() {
        String id = "1";
        RouteDto mockRoute = new RouteDto();
        mockRoute.setId(1L);

        when(routeService.getRouteById(1L)).thenReturn(mockRoute);

        ResponseEntity<BaseRes<RouteDto>> response = routeController.getRouteById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Route fetched successfully", response.getBody().getMessage());
        assertEquals(1L, response.getBody().getItem().getId());

        verify(routeService, times(1)).getRouteById(1L);
    }

    @Test
    void testCreateRoute() {
        RouteDto routeDto = new RouteDto();
        routeDto.setTitle("New Route");

        RouteDto createdRoute = new RouteDto();
        createdRoute.setId(1L);
        createdRoute.setTitle("New Route");

        when(routeService.createRoute(routeDto)).thenReturn(createdRoute);

        ResponseEntity<BaseRes<RouteDto>> response = routeController.createRoute(routeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Route created successfully", response.getBody().getMessage());
        assertEquals(1L, response.getBody().getItem().getId());

        verify(routeService, times(1)).createRoute(routeDto);
    }

    @Test
    void testUpdateRoute() {
        String id = "1";
        RouteDto routeDto = new RouteDto();
        routeDto.setTitle("Updated Route");

        RouteDto updatedRoute = new RouteDto();
        updatedRoute.setId(1L);
        updatedRoute.setTitle("Updated Route");

        when(routeService.updateRoute(1L, routeDto)).thenReturn(updatedRoute);

        ResponseEntity<BaseRes<RouteDto>> response = routeController.updateRoute(id, routeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Route updated successfully", response.getBody().getMessage());
        assertEquals("Updated Route", response.getBody().getItem().getTitle());

        verify(routeService, times(1)).updateRoute(1L, routeDto);
    }

    @Test
    void testDeleteRoute() {
        String id = "1";

        doNothing().when(routeService).deleteRoute(1L);

        ResponseEntity<BaseRes<String>> response = routeController.deleteRoute(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Route deleted successfully", response.getBody().getMessage());

        verify(routeService, times(1)).deleteRoute(1L);
    }
}
