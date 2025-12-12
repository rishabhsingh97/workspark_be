package com.workspark.userservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private String message;
    private String error;
    private T data;
    private String token;
    private String userId;
    private boolean isValid;
}
