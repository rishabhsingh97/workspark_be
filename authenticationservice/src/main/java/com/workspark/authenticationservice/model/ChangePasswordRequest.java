package com.workspark.authenticationservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

	@NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Old password cannot be null")
    @NotEmpty(message = "Old password cannot be empty")
    @Size(min = 6, message = "Old password must be at least 6 characters long")
    private String oldPassword;

    @NotNull(message = "New password cannot be null")
    @NotEmpty(message = "New password cannot be empty")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
    message = "New password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String newPassword;
}
