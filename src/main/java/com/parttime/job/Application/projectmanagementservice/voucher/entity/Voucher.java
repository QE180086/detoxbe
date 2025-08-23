package com.parttime.job.Application.projectmanagementservice.voucher.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher extends BaseEntity {
    @Column(unique = true)
    private String code;
    private double discountValue;
    private boolean isPercentage;
    private double minOrderValue;
    private boolean isActive;
    private String image;
}
