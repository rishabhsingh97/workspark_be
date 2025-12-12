package com.workspark.userservice.model.dto.response;

import com.workspark.models.enums.Status;
import com.workspark.models.enums.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class SignInResponse {

	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String password;
	private Status status;
	private List<UserRole> roles;

}
