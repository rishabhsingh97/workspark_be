package com.workspark.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ApiResponce<T> {

	private String message;
	private String error;
	private T data;
	private String token;
	private String userId;
	private boolean isValid;
}
