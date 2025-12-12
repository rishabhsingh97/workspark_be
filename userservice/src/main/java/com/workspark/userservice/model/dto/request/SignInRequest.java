package com.workspark.userservice.model.dto.request;

import lombok.Data;

@Data
public class SignInRequest {
	
	private String email;
	private String password;

}
