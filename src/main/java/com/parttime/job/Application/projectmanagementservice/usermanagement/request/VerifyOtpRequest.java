package com.parttime.job.Application.projectmanagementservice.usermanagement.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank(message = "Email is not blank t send OTP.")
    private String email;
    @NotBlank(message = "OTP is not null to verify account.")
    private String otp;
}
