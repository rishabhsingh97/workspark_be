package com.workspark.userservice.model.dto;

import com.workspark.models.enums.Status;
import com.workspark.userservice.model.entitity.TenantUserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class TenantUserDto implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Status status;
    private Date createdAt;
    private Date updatedAt;
    private Boolean sso;
    private List<TenantUserRoles> roles;

}
