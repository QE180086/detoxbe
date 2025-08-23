package com.parttime.job.Application.projectmanagementservice.usermanagement.response;

import lombok.Data;

@Data
public class AuthResponseLogin {
    private String accessToken;
    private String userId;
    private String username;
    private String email;
}
