package com.workspark.authenticationservice.model;

import java.io.Serializable;
import java.util.List;

import com.workspark.models.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigninResponse implements Serializable	{

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private Boolean status;
    private List<UserRoleEnum> roles;


}
