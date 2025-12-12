package com.workspark.userservice.service;

import com.workspark.models.request.SignupRequest;
import com.workspark.userservice.model.dto.response.TenantResponse;

import java.util.List;

public interface TenantService {

    void addTenantAdmin(SignupRequest request);
    List<TenantResponse> getAllTenant();
}
