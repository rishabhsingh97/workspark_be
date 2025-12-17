package com.workspark.models.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    SUPER_ADMIN,
    TENANT_ADMIN,
    SYSTEM,
    ADMIN,
    USER
}
