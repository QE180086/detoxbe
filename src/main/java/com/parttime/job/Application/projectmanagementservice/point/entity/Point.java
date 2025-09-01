package com.parttime.job.Application.projectmanagementservice.point.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Point extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    private User user;

    private int currentPoints;

}
