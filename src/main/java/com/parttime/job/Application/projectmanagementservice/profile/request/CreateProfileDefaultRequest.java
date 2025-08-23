package com.parttime.job.Application.projectmanagementservice.profile.request;

import com.parttime.job.Application.projectmanagementservice.profile.enumration.Gender;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import lombok.Data;


@Data
public class CreateProfileDefaultRequest {
    private String fullName;
    private Gender gender;
    private User user;
}
