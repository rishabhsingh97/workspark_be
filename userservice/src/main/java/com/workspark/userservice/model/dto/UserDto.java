package com.workspark.userservice.model.dto;

import com.workspark.models.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<UserRoleEnum> roles;

}
