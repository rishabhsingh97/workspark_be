package com.workspark.masterservice.model.mapper;

import com.workspark.masterservice.model.dto.TenantDto;
import com.workspark.masterservice.model.entitiy.Tenant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    TenantDto toDto(Tenant tenant);
    Tenant toEntity(TenantDto dto);

}