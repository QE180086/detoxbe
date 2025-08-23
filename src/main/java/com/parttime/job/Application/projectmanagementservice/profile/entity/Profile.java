package com.parttime.job.Application.projectmanagementservice.profile.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.profile.enumration.Gender;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile extends BaseEntity {
    private String nickName;
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Information information;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
