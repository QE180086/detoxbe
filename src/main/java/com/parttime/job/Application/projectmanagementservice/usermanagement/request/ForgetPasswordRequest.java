package com.parttime.job.Application.projectmanagementservice.usermanagement.request;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
    private String email;
    private String username;
}
