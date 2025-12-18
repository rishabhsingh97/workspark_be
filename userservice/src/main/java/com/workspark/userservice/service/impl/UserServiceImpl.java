package com.workspark.userservice.service.impl;

import com.workspark.models.enums.UserRoleEnum;
import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.SignInResponse;
import com.workspark.userservice.exceptions.customExceptions.UserAlreadyExistsException;
import com.workspark.userservice.exceptions.customExceptions.UserNotFoundException;
import com.workspark.userservice.model.dto.UserDto;
import com.workspark.userservice.model.entity.Role;
import com.workspark.userservice.model.entity.User;
import com.workspark.userservice.model.entity.UserRoleMapping;
import com.workspark.userservice.model.mapper.UserMapper;
import com.workspark.userservice.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.workspark.userservice.repo.UserRepo;
import com.workspark.userservice.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepo roleRepo;

    /**
     * Adds a new tenant user and assigns roles to the user.
     *
     * @param userDto The {@link SignupRequest} containing user details.
     * @throws RuntimeException If a user with the same email already exists.
     */
    @Override
    @Transactional
    public void addUser(UserDto userDto) {

        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists in the tenant.");
        }

        // Create user
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        // Assign roles
        for (UserRoleEnum roleEnum : userDto.getRoles()) {

            Role role = roleRepo.findByName(roleEnum)
                            .orElseThrow(() ->
                                    new RuntimeException("Role not found: " + roleEnum)
                            );

            if(role.getStatus() != 0) {
                UserRoleMapping userRoleMapping = new UserRoleMapping();
                userRoleMapping.setUser(newUser);
                userRoleMapping.setRole(role);
                newUser.getUserRoleMappings().add(userRoleMapping);
            }
        }
        userRepo.save(newUser);
        log.info("Added user with roles: {}", userDto.getEmail());
    }


    @Override
    public UserDto getUser(String id) {
        return userMapper.toDto(userRepo.findById(UUID.fromString(id))
                .orElse(null));
    }

    /**
     * Retrieves all tenant users and returns their details.
     *
     * @return A list of {@link SignInResponse} containing tenant user details.
     */
    @Override
    public Page<UserDto> getAllUsers(int page, int size) {
        return userRepo
                .findAll(PageRequest.of(page - 1, size))
                .map(userMapper::toDto);
    }


    /**
     * Updates the details of an existing tenant user.
     *
     * @param request The {@link SignupRequest} containing updated user information.
     */
    @Override
    public UserDto updateUser(SignupRequest request) {
      return null;
    }

    @Override
    public UserDto deleteUser(SignupRequest request) {
        Optional<User> userOptional = userRepo.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOptional.get();
        userRepo.save(user);

        // Save the updated user
        return userMapper.toDto(user);
    }

}
