package com.workspark.userservice.model.dto;

import com.workspark.models.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDto implements Serializable {
    private Long id;
    private String path;
    private String title;
    private List<UserRole> roles;
}


