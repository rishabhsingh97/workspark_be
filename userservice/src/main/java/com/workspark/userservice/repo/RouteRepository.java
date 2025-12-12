package com.workspark.userservice.repo;

import com.workspark.userservice.model.entitity.Role;
import com.workspark.userservice.model.entitity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsByPath(String path);
    Page<Route> findRoutesByRolesIn(List<Role> roles, Pageable pageable);

}
