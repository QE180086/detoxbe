package com.parttime.job.Application.projectmanagementservice.usermanagement.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestRegister {
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one number."
    )
    private String password;

    @Email(message = "Invalid email format.")
    private String email;
}
