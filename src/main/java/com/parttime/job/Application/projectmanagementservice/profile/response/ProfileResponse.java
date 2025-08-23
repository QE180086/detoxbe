package com.parttime.job.Application.projectmanagementservice.profile.response;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Address;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Information;
import com.parttime.job.Application.projectmanagementservice.profile.enumration.Gender;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProfileResponse {
    private String id;
    private Date createdDate;
    private Date updatedDate;
    private String nickName;
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String avatar;
    private Gender gender;
    private List<Address> addresses;
    private Information information;

    private String userId;
    private String username;

}
