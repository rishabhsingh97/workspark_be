package com.workspark.userservice.service;

import com.workspark.models.request.SignupRequest;
import com.workspark.models.response.BasePageRes;
import com.workspark.models.response.SignInResponse;
import com.workspark.userservice.model.dto.TenantUserDto;
import org.springframework.stereotype.Service;

@Service
public interface TenantUserService {

	void addTenantUser(SignupRequest request);

	BasePageRes<SignInResponse> getAllUsers(int page, int size);

	SignInResponse getUserDetails(String key, String value);

	TenantUserDto getUserDetailsById(Long id);

	SignInResponse updateTenantUser(SignupRequest request);

	SignInResponse deactivateTenantUser(SignupRequest request);
}
