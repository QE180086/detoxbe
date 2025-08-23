package com.parttime.job.Application.projectmanagementservice.point.entity;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Point {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    private User user;

    private int currentPoints;

    @OneToMany
    @JoinColumn(name = "voucher_id", nullable = false)
    private List<Voucher> vouchers;
}
