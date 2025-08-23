package com.parttime.job.Application.projectmanagementservice.usermanagement.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPassword {
    private String otp;
    private String email;
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one number."
    )
    private String newPassword;
}
