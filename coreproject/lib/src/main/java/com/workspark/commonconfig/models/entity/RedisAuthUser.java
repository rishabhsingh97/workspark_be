package com.workspark.commonconfig.models.entity;

import com.workspark.models.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("auth_user")
public class RedisAuthUser implements Serializable {

    @Id
    private String redisUserId;
    private AuthUser authUser;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class AuthUser {
        private Long id;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String email;
        private String password;
        private List<UserRoleEnum> roles;
    }
}
