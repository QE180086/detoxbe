package com.parttime.job.Application.projectmanagementservice.usermanagement.service;

import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UserRequestLogin;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UserRequestRegister;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.AuthResponseLogin;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    AuthResponseLogin login(UserRequestLogin request);

    UserResponse register(UserRequestRegister register) throws MessagingException;

}
