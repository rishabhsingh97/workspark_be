package com.workspark.masterservice.service;

import com.workspark.masterservice.model.dto.TenantDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TenantService {

    TenantDto addTenant(TenantDto tenantDto);
    TenantDto getTenant(String id);
    List<TenantDto> getAllTenants(int page, int size);
    TenantDto updateTenant(String id, TenantDto tenantDto);
    TenantDto deleteTenant(String id);

}
