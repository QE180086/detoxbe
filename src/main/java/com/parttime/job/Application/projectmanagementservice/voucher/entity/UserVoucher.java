package com.parttime.job.Application.projectmanagementservice.voucher.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "user_voucher",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "voucher_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVoucher extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    private boolean used;
}

