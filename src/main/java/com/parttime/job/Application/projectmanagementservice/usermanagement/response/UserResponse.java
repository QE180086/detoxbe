package com.parttime.job.Application.projectmanagementservice.usermanagement.response;

import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String password;
    private String email;
    private UserStatus status;
    private boolean isDeleted;
    private String role;
}
