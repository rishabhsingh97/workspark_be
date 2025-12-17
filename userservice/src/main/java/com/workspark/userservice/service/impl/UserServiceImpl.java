package com.workspark.userservice.service.serviceImpl;

import com.workspark.models.enums.Status;
import com.workspark.models.enums.UserRole;
import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BasePageRes;
import com.workspark.models.response.SignInResponse;
import com.workspark.userservice.exceptions.customExceptions.UserAlreadyExistsException;
import com.workspark.userservice.exceptions.customExceptions.UserNotFoundException;
import com.workspark.userservice.model.dto.TenantUserDto;
import com.workspark.userservice.model.entitity.TenantUserRoles;
import com.workspark.userservice.model.entitity.TenantUser;
import com.workspark.userservice.repo.TenantUserRolesMapRepository;
import com.workspark.userservice.repo.TenantUserRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.workspark.userservice.repo.TenantUserRepository;
import com.workspark.userservice.service.TenantUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantUserServiceImpl implements TenantUserService {

    private final TenantUserRepository tenantUserRepository;
    private final TenantUserRolesRepository tenantUserRolesRepository;
    private final TenantUserRolesMapRepository tenantUserRolesMapRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Adds a new tenant user and assigns roles to the user.
     *
     * @param request The {@link SignupRequest} containing user details.
     * @throws RuntimeException If a user with the same email already exists.
     */
    @Override
    public void addTenantUser(SignupRequest request) {
        if (tenantUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists in the tenant.");
        }
        // Create new user
        TenantUser newUser = new TenantUser();
        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));  // Make sure to encrypt password
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setStatus(Status.PENDING);
        Date now = new Date();
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);
        List<TenantUserRoles> roles = new ArrayList<>();
        for (UserRole role : request.getRoles()) {
            TenantUserRoles userRole = tenantUserRolesRepository.findByRoleName(role.toString());
            roles.add(userRole);
        }
        newUser.setRoles(roles);
        // Save the new basic user to the tenant_users table
        tenantUserRepository.save(newUser);

        log.info("Added basic user: {}", request.getEmail());

    }

    /**
     * Retrieves all tenant users and returns their details.
     *
     * @return A list of {@link SignInResponse} containing tenant user details.
     */
    @Override
    public BasePageRes<SignInResponse> getAllUsers(int page, int size) {
        Page<TenantUser> userPage = tenantUserRepository.findAll(PageRequest.of(page - 1, size));

        return BasePageRes.<SignInResponse>builder()
                .data(userPage.getContent().stream().map(this::toSigninResponse).toList())
                .pageNo(page)
                .pageSize(size)
                .totalPages(userPage.getTotalPages())
                .totalCount(userPage.getTotalElements())
                .build();
    }

    /**
     * Retrieves a tenant user's details based on a specified key-value pair.
     *
     * @param key   The key to search for (e.g., "id" or "email").
     * @param value The value to match.
     * @return The {@link SignInResponse} containing the user's details.
     * @throws IllegalArgumentException If the key is invalid.
     */
    @Transactional
    @Override
    public SignInResponse getUserDetails(String key, String value) {
        return
                switch (key) {
                    case "id" -> {
                        Long id = Long.parseLong(value);
                        yield this.getUserById(id);
                    }
                    case "email" -> this.getUserByEmail(value);

                    default -> throw new IllegalArgumentException("Invalid key: " + key);
                };
    }

    public TenantUserDto getUserDetailsById(Long id) {
        TenantUser userDetails = tenantUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        return toDto(userDetails);
    }

    private TenantUserDto toDto(TenantUser tenantUser) {
        return TenantUserDto.builder()
                .id(tenantUser.getId())
                .firstName(tenantUser.getFirstName())
                .lastName(tenantUser.getLastName())
                .email(tenantUser.getEmail())
                .phoneNumber(tenantUser.getPhoneNumber())
                .password(tenantUser.getPassword())
                .status(tenantUser.getStatus())
                .createdAt(tenantUser.getCreatedAt())
                .updatedAt(tenantUser.getUpdatedAt())
                .sso(tenantUser.getSso())
                .roles(tenantUser.getRoles())
                .build();
    }


    /**
     * Updates the details of an existing tenant user.
     *
     * @param request The {@link SignupRequest} containing updated user information.
     */
    @Override
    public SignInResponse updateTenantUser(SignupRequest request) {
        Optional<TenantUser> userOptional = tenantUserRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        TenantUser user = userOptional.get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        List<TenantUserRoles> roles = new ArrayList<>();
        for (UserRole role : request.getRoles()) {
            TenantUserRoles userRole = tenantUserRolesRepository.findByRoleName(role.toString());
            roles.add(userRole);
        }
        user.setRoles(roles);
        tenantUserRepository.save(user);

        // Save the updated user
        return toSigninResponse(user);
    }

    @Override
    public SignInResponse deactivateTenantUser(SignupRequest request) {
        Optional<TenantUser> userOptional = tenantUserRepository.findByEmail(request.getEmail());
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        TenantUser user = userOptional.get();
        user.setStatus(Status.INACTIVE);
        tenantUserRepository.save(user);

        // Save the updated user
        return toSigninResponse(user);
    }

    /**
     * Retrieves a tenant user's details by their ID.
     *
     * @param id The ID of the user.
     * @return The {@link SignInResponse} containing the user's details.
     * @throws RuntimeException If the user is not found.
     */
    private SignInResponse getUserById(Long id) {
        TenantUser userDetails = tenantUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (Status.INACTIVE.equals(userDetails.getStatus())) {
            throw new UserNotFoundException("User is inactive");
        } else {
            userDetails.setStatus(Status.ACTIVE);
        }

        return toSigninResponse(userDetails);
    }

    /**
     * Retrieves a tenant user's details by their email.
     *
     * @param email The email of the user.
     * @return The {@link SignInResponse} containing the user's details.
     * @throws RuntimeException If the user is not found.
     */
    public SignInResponse getUserByEmail(String email) {
        Optional<TenantUser> userOptional = tenantUserRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with this email id does not exist");
        }
        TenantUser user = userOptional.get();
        if (Status.INACTIVE.equals(user.getStatus())) {
            throw new UserNotFoundException("User is inactive");
        } else {
            user.setStatus(Status.ACTIVE);
        }
        return toSigninResponse(user);
    }

    /**
     * Converts a {@link TenantUser} object to a {@link SignInResponse}.
     *
     * @param tenantUsers The user entity to be converted.
     * @return The {@link SignInResponse} representing the user.
     */
    public SignInResponse toSigninResponse(TenantUser tenantUsers) {
        SignInResponse response = new SignInResponse();
        response.setUserId(tenantUsers.getId());
        response.setFirstName(tenantUsers.getFirstName());
        response.setLastName(tenantUsers.getLastName());
        response.setEmail(tenantUsers.getEmail());
        response.setPhoneNumber(tenantUsers.getPhoneNumber());
        response.setPassword(tenantUsers.getPassword());
        response.setStatus(tenantUsers.getStatus());
        List<String> roles = tenantUsers.getRoles().stream().map(TenantUserRoles::getRoleName).toList();
        List<UserRole> roleEnums = roles.stream().map(UserRole::valueOf).collect(Collectors.toList());
        response.setRoles(roleEnums);
        return response;
    }


}
