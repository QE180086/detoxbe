package com.parttime.job.Application.projectmanagementservice.usermanagement.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "role_name")
    private Role role;

    @OneToOne(mappedBy = "user")
    Profile profile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getId() != null && getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
