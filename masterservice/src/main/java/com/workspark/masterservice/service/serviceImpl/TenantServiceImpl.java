package com.workspark.masterservice.service.serviceImpl;

import com.workspark.masterservice.model.dto.TenantDto;
import com.workspark.masterservice.model.entitiy.Tenant;
import com.workspark.masterservice.model.mapper.TenantMapper;
import com.workspark.masterservice.repo.TenantRepo;
import com.workspark.masterservice.service.LiquibaseService;
import com.workspark.masterservice.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepo tenantRepo;
    private final LiquibaseService liquibaseService;
    private final TenantMapper tenantMapper;

    @Override
    @Transactional
    public TenantDto addTenant(TenantDto tenantDto) {
        Tenant tenant = tenantMapper.toEntity(tenantDto);
        log.info(">> ten: {}", tenant);
        liquibaseService.initTenantSchema(tenant);
        tenant.setStatus(1);
        log.info(">> before save {}", tenant);
        Tenant saved = tenantRepo.save(tenant);
        log.info(">> after save {}", saved);
        return tenantMapper.toDto(saved);
    }

    @Override
    public TenantDto getTenant(String id) {
        Tenant entity = tenantRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        return tenantMapper.toDto(entity);
    }

    @Override
    public List<TenantDto> getAllTenants(int page, int size) {
        Page<Tenant> result = tenantRepo.findAll(
                PageRequest.of(page, size)
        );

        return result.getContent()
                .stream()
                .map(tenantMapper::toDto)
                .toList();
    }

    @Override
    public TenantDto updateTenant(String id, TenantDto tenantDto) {
        Tenant entity = tenantRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        entity.setName(tenantDto.getName());

        return tenantMapper.toDto(entity); // managed entity, auto-flushed
    }

    @Override
    public TenantDto deleteTenant(String id) {
        Tenant entity = tenantRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        tenantRepo.delete(entity);
        return tenantMapper.toDto(entity);
    }

}
