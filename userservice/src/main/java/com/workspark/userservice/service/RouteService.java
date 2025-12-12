package com.workspark.userservice.service;

import com.workspark.models.response.PaginatedRes;
import com.workspark.userservice.model.dto.RouteDto;
import org.springframework.stereotype.Service;

@Service
public interface RouteService {

    PaginatedRes<RouteDto> getAllRoutes(int page, int size);

    RouteDto createRoute(RouteDto routeDataDto);

    RouteDto updateRoute(Long id, RouteDto routeDataDto);

    void deleteRoute(Long id);

    RouteDto getRouteById(Long id);
}
