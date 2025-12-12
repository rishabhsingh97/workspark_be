package com.workspark.userservice.repo;

import com.workspark.models.enums.UserRole;
import com.workspark.userservice.model.entitity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    List<Role> findMasterUserRolesByRoleNameIn(List<UserRole> roles);

}
