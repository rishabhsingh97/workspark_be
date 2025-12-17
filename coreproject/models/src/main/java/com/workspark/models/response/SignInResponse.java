package com.workspark.models.response;

import com.workspark.models.enums.Status;
import com.workspark.models.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse implements Serializable {

        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String password;
        private Status status;
        private List<UserRoleEnum> roles;

}

