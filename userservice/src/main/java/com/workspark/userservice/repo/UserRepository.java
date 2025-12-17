package com.workspark.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workspark.userservice.model.entitity.TenantUser;

import java.util.Optional;

@Repository
public interface TenantUserRepository extends JpaRepository<TenantUser, Long>{

    Optional<TenantUser> findByEmail(String email);
}
