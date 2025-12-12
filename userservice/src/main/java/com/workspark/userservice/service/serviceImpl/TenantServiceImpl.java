package com.workspark.userservice.service.serviceImpl;

import com.workspark.models.enums.Status;
import com.workspark.models.enums.UserRole;
import com.workspark.models.request.SignupRequest;
import com.workspark.userservice.exceptions.customExceptions.UserAlreadyExistsException;
import com.workspark.userservice.model.dto.response.TenantResponse;
import com.workspark.userservice.model.entitity.TenantUserRoles;
import com.workspark.userservice.model.entitity.TenantUser;
import com.workspark.userservice.repo.TenantConfigRepository;
import com.workspark.userservice.repo.TenantUserRepository;
import com.workspark.userservice.repo.TenantUserRolesMapRepository;
import com.workspark.userservice.repo.TenantUserRolesRepository;
import com.workspark.userservice.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing tenant-related user operations.
 * This includes adding a new tenant admin, assigning roles, and managing user details.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantUserRepository tenantUserRepository;

    @Autowired
    private TenantUserRolesMapRepository tenantUserRolesMapRepository;

    @Autowired
    private TenantUserRolesRepository tenantUserRolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final TenantConfigRepository tenantConfigRepository;

    /**
     * Adds a new tenant admin or assigns the role to an existing user.
     *
     * @param request The user's details to be signed up as a tenant admin.
     * @throws RuntimeException If a tenant admin already exists.
     */
    @Override
    public void addTenantAdmin(SignupRequest request) {
        TenantUser users = new TenantUser();
        Optional<TenantUser> existingUser = tenantUserRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User Already exists with this email.");
        } else {
            users.setEmail(request.getEmail());
            users.setFirstName(request.getFirstName());
            users.setLastName(request.getLastName());
            users.setPassword(passwordEncoder.encode(request.getPassword()));
            users.setPhoneNumber(request.getPhoneNumber());
            users.setSso(request.isSso());
            users.setStatus(Status.ACTIVE);
            Date now = new Date();
            users.setCreatedAt(now);
            users.setUpdatedAt(now);
            List<TenantUserRoles> roles = new ArrayList<>();
            TenantUserRoles role = null;
            if(request.getRoles() == null || request.getRoles().isEmpty()) {
                role = tenantUserRolesRepository.findByRoleName(UserRole.TENANT_ADMIN.toString());
            } else {
                for (UserRole roleName : request.getRoles()) {
                    role = tenantUserRolesRepository.findByRoleName(roleName.toString());
                }
            }
            roles.add(role);
            users.setRoles(roles);
            tenantUserRepository.save(users);
            log.info("Signup request");
            log.info("Assigning tenant admin role to the user {}..", users.getEmail());
        }
    }

    @Override
    public List<TenantResponse> getAllTenant() {

            // Fetch the list of tenant database schemas from the repository
            List<String> tenantSchemas = tenantConfigRepository.findAllTenantDbSchemas();

            // Iterate over the list of tenant schemas and map to TenantResponse DTO
            return tenantSchemas.stream()
                    .map(schema -> new TenantResponse(schema))  // Mapping to DTO
                    .collect(Collectors.toList());

        }
    }

