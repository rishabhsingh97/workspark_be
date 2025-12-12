package com.workspark.userservice.service.serviceImpl;

import com.workspark.models.enums.UserRole;
import com.workspark.models.pojo.AuthUser;
import com.workspark.models.response.PaginatedRes;
import com.workspark.userservice.model.dto.RouteDto;
import com.workspark.userservice.model.entitity.Route;
import com.workspark.userservice.model.entitity.Role;
import com.workspark.userservice.model.enums.ErrorMessages;
import com.workspark.userservice.repo.RouteRepository;
import com.workspark.userservice.repo.RoleRepository;
import com.workspark.userservice.service.RouteService;
import com.workspark.userservice.exceptions.customExceptions.RouteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeDataRepository;
    private final RoleRepository masterUserRolesRepository;

    @Override
    @Transactional
    public PaginatedRes<RouteDto> getAllRoutes(int page, int size) {
        log.info("Fetching all routes - page: {}, size: {}", page - 1, size);

        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("user: {}", user.getRoles());

        List<Role> roles = masterUserRolesRepository.findMasterUserRolesByRoleNameIn(user.getRoles());
        log.info("roles: {}", roles);

        Page<Route> routeDataPage = routeDataRepository.findRoutesByRolesIn(roles, PageRequest.of(page - 1, size));
        log.info("routeDataPage: {}", routeDataPage);

        return PaginatedRes.<RouteDto>builder()
                .data(routeDataPage.getContent().stream().map(this::mapToDto).toList())
                .pageNo(page)
                .pageSize(size)
                .totalPages(routeDataPage.getTotalPages())
                .totalCount(routeDataPage.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public RouteDto createRoute(RouteDto routeDto) {
        log.info("Creating route: {}", routeDto);
        validatePath(routeDto.getPath());
        List<Role> roles = validateRoles(routeDto.getRoles());

        Route route = Route.builder()
                .title(routeDto.getTitle())
                .path(routeDto.getPath())
                .roles(roles)
                .build();

        return mapToDto(routeDataRepository.save(route));
    }

    @Override
    @Transactional
    public RouteDto updateRoute(Long id, RouteDto routeDto) {
        log.info("Updating route with id: {}", id);
        Route route = findRouteById(id);
        if (!Objects.equals(route.getPath(), routeDto.getPath())) validatePath(routeDto.getPath());
        route.setRoles(validateRoles(routeDto.getRoles()));
        route.setTitle(routeDto.getTitle());
        route.setPath(routeDto.getPath());
        return mapToDto(routeDataRepository.save(route));
    }

    @Override
    public void deleteRoute(Long id) {
        log.info("Deleting route with id: {}", id);
        routeDataRepository.deleteById(findRouteById(id).getId());
    }

    @Override
    public RouteDto getRouteById(Long id) {
        return mapToDto(findRouteById(id));
    }

    private Route findRouteById(Long id) {
        return routeDataRepository.findById(id)
                .orElseThrow(() -> new RouteException(ErrorMessages.ROUTE_NOT_FOUND.arguments(id)));
    }

    private void validatePath(String path) {
        if (routeDataRepository.existsByPath(path)) {
            throw new RouteException(ErrorMessages.ROUTE_ALREADY_EXISTS.arguments(path));
        }
    }

    private List<Role> validateRoles(List<UserRole> roleNames) {
        List<Role> roles = masterUserRolesRepository.findMasterUserRolesByRoleNameIn(roleNames);
        if (roles.size() != roleNames.size()) {
            throw new RouteException(ErrorMessages.INVALID_ROLE.arguments(roleNames));
        }
        return roles;
    }

    private RouteDto mapToDto(Route route) {
        return RouteDto.builder()
                .id(route.getId())
                .title(route.getTitle())
                .path(route.getPath())
                .roles(Stream.ofNullable(route.getRoles())
                        .flatMap(List::stream)
                        .map(Role::getRoleName)
                        .toList()
                )
                .build();
    }
}
