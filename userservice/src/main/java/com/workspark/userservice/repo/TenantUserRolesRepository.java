package com.workspark.userservice.repo;

import com.workspark.models.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import com.workspark.userservice.model.entitity.TenantUserRoles;

import java.util.List;

//@Repository
public interface TenantUserRolesRepository extends JpaRepository<TenantUserRoles, Long>{

    TenantUserRoles findById(Integer roleId);
    // Custom method to find roles by role names
    List<UserRole> findByRoleNameIn(List<String> roleNames);
    TenantUserRoles findByRoleName(String roleName);
}
