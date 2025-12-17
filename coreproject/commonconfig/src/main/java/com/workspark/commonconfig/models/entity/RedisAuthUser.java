package com.workspark.commonconfig.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("auth_user")
public class RedisAuthUser implements Serializable {

    @Id
    private String redisUserId;
    private AuthUser authUser;

}
