package com.workspark.userservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    ROUTE_ERROR("Route error %s"),
    ROUTE_NOT_FOUND("Route not found with id %s"),
    ROUTE_ALREADY_EXISTS("Route already exists with path %s"),

    INVALID_ROLE("Invalid role %s");

    private final String message;

    public String arguments(Object... args) {
        return String.format(this.message, args);
    }

}
