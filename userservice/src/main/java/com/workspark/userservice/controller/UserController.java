package com.workspark.userservice.controller;

import com.workspark.models.response.BaseRes;
import com.workspark.models.response.BasePageRes;
import com.workspark.userservice.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.workspark.userservice.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a tenant user's details based on a key-value pair.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaseRes<UserDto>> getUser(@PathVariable  String id) {
        log.info("Received request to get user with key: {}", id);

        UserDto response = userService.getUser(id);

        log.info("Successfully fetched user details for key: {}", id);
        return BaseRes.success(response, "User details retrieved successfully.", HttpStatus.OK);
    }

    /**
     * Adds a new tenant user.
     */
    @PostMapping
    public ResponseEntity<BaseRes<String>> addUser(@RequestBody UserDto userDto) {

        log.info("Starting signup process for user: {}", userDto.getEmail());
        userService.addUser(userDto);
        log.info("Signup successful for user: {}", userDto.getEmail());
        return BaseRes.success("User Signup successful", "User created successfully.", HttpStatus.OK);

    }

    /**
     * Retrieves all tenant users with pagination.
     */
    @GetMapping
    public ResponseEntity<BasePageRes<UserDto>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        log.info("Fetching all tenant users with pagination: page={}, size={}", page, size);
        Page<UserDto> userDtoList = userService.getAllUsers(page, size);
        return BasePageRes.success(userDtoList.getContent(), "Tenant users retrieved successfully.", HttpStatus.OK);
    }

}
