package com.workspark.userservice.repo;

import com.workspark.models.enums.UserRoleEnum;
import com.workspark.userservice.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long>{

    Optional<Role> findByName(UserRoleEnum name);

}
