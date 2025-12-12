package com.workspark.userservice.controller;

import com.workspark.models.response.BaseRes;
import com.workspark.models.response.PaginatedRes;
import com.workspark.userservice.model.dto.RouteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * This controllers handle the route roles specific to the frontend.
 * It is used to create, update, delete and get routes.
 * It is also used to get all routes.
 * It is used to get all routes by tags.
 * It is used to get a route by id.
 */
@RequestMapping("/api/v1/routes")
public interface RouteController {

    @GetMapping
    ResponseEntity<BaseRes<PaginatedRes<RouteDto>>> getAllRoutes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size);

    @GetMapping("/{id}")
    ResponseEntity<BaseRes<RouteDto>> getRouteById(@PathVariable String id);

    @PostMapping
    ResponseEntity<BaseRes<RouteDto>> createRoute(@RequestBody RouteDto routeDataDto);

    @PutMapping("/{id}")
    ResponseEntity<BaseRes<RouteDto>> updateRoute(
            @PathVariable String id, @RequestBody RouteDto routeDataDto);

    @DeleteMapping("/{id}")
    ResponseEntity<BaseRes<String>> deleteRoute(@PathVariable String id);

}
