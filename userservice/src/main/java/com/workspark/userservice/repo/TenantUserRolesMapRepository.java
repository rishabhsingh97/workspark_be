package com.workspark.userservice.repo;

import com.workspark.userservice.model.entitity.TenantUserRoles;
import com.workspark.userservice.model.entitity.TenantUserRolesMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantUserRolesMapRepository extends JpaRepository<TenantUserRolesMap, Long> {

    Optional<TenantUserRolesMap> findByUserId(Long id);
    Optional<TenantUserRolesMap> findByRoleId(Integer id);

    List<TenantUserRoles> findAllByUserId(Long userId);
}
