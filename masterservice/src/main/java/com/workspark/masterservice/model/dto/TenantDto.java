package com.workspark.masterservice.model.dto;

import com.workspark.models.dto.BaseTenantDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDto extends BaseTenantDto  {

    private String name;
    private String domain;
    private String db;
    private String email;
}
