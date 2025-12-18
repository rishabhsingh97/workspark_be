package com.workspark.userservice.service;

import com.workspark.models.request.SignupRequest;
import com.workspark.userservice.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

	void addUser(UserDto userDto);

	UserDto getUser(String id);

	Page<UserDto> getAllUsers(int page, int size);

	UserDto updateUser(SignupRequest request);

	UserDto deleteUser(SignupRequest request);
}
