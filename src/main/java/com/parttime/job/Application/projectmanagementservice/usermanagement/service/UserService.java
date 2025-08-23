package com.parttime.job.Application.projectmanagementservice.usermanagement.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ChangePasswordRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ForgetPasswordRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ResetPassword;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UpdateRoleRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;

public interface UserService {
    PagingResponse<UserResponse> getAllUser(String searchText, PagingRequest request);

    UserResponse changePassword(ChangePasswordRequest request);

    void forgetPassword(ForgetPasswordRequest request);

    UserResponse resetPassword(ResetPassword resetPassword);

    UserResponse getDetailUser(String userId);

    UserResponse assignRole(String userId, UpdateRoleRequest role);

    UserResponse setStatus(String userId, UserStatus status);

    UserResponse setDelete(String userId, boolean isDelete);

    void deleteUser(String userId);

}
