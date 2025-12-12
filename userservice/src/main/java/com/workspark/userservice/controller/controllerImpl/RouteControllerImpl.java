package com.workspark.userservice.controller.controllerImpl;

import com.workspark.commonconfig.models.pojo.TenantContext;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import com.workspark.userservice.controller.RouteController;
import com.workspark.userservice.model.dto.RouteDto;
import com.workspark.userservice.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Implementation of the RouteController interface.
 * Handles HTTP requests related to route management.
 * Ensures tenant context is set to MASTER_TENANT_SCHEMA for overriding current host datasource.
 * Logs all incoming requests and responses for better traceability.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RouteControllerImpl implements RouteController {

    private final RouteService routeService;
    private static final String MASTER_TENANT_SCHEMA = "workspark";

    /**
     * Retrieves all routes with pagination and optional tag filtering.
     *
     * @param page The page number to retrieve, default is 1.
     * @param size The number of items per page, default is 50.
     * @return A paginated list of routes wrapped in a success response.
     */
    public ResponseEntity<BaseRes<PaginatedRes<RouteDto>>> getAllRoutes(
            int page,
            int size) {

        log.info("Request to fetch all routes: page={}, size={}, tags={}", page, size);
        TenantContext.setCurrentTenant(MASTER_TENANT_SCHEMA);

        try {
            PaginatedRes<RouteDto> paginatedRes = routeService.getAllRoutes(page, size);
            log.info("Successfully fetched routes, totalItems={}", paginatedRes.getPageSize());
            return BaseRes.success(paginatedRes, "Routes fetched successfully", HttpStatus.OK);
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Retrieves a route by its ID.
     *
     * @param id The ID of the route to retrieve.
     * @return The route data wrapped in a success response.
     */
    public ResponseEntity<BaseRes<RouteDto>> getRouteById(String id) {

        log.info("Request to fetch route with ID: {}", id);
        TenantContext.setCurrentTenant(MASTER_TENANT_SCHEMA);

        try {
            RouteDto routeDataDto = routeService.getRouteById(Long.parseLong(id));
            log.info("Successfully fetched route with ID: {}", id);
            return BaseRes.success(routeDataDto, "Route fetched successfully", HttpStatus.OK);
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Creates a new route.
     *
     * @param routeDataDto The route data to create.
     * @return The created route data wrapped in a success response.
     */
    public ResponseEntity<BaseRes<RouteDto>> createRoute(RouteDto routeDataDto) {

        log.info("Request to create route: {}", routeDataDto);
        TenantContext.setCurrentTenant(MASTER_TENANT_SCHEMA);

        try {
            RouteDto createdRoute = routeService.createRoute(routeDataDto);
            log.info("Successfully created route with ID: {}", createdRoute.getId());
            return BaseRes.success(createdRoute, "Route created successfully", HttpStatus.OK);
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Updates an existing route.
     *
     * @param id           The ID of the route to update.
     * @param routeDataDto The updated route data.
     * @return The updated route data wrapped in a success response.
     */
    public ResponseEntity<BaseRes<RouteDto>> updateRoute(
            String id,
            RouteDto routeDataDto) {

        log.info("Request to update route with ID: {}, Data: {}", id, routeDataDto);
        TenantContext.setCurrentTenant(MASTER_TENANT_SCHEMA);

        try {
            RouteDto updatedRoute = routeService.updateRoute(Long.parseLong(id), routeDataDto);
            log.info("Successfully updated route with ID: {}", id);
            return BaseRes.success(updatedRoute, "Route updated successfully", HttpStatus.OK);
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Deletes a route by its ID.
     *
     * @param id The ID of the route to delete.
     * @return A success message wrapped in a success response.
     */
    public ResponseEntity<BaseRes<String>> deleteRoute(String id) {

        log.info("Request to delete route with ID: {}", id);
        TenantContext.setCurrentTenant(MASTER_TENANT_SCHEMA);

        try {
            routeService.deleteRoute(Long.parseLong(id));
            log.info("Successfully deleted route with ID: {}", id);
            return BaseRes.success("", "Route deleted successfully", HttpStatus.OK);
        } finally {
            TenantContext.clear();
        }
    }
}
