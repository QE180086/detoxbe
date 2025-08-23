package com.parttime.job.Application.projectmanagementservice.usermanagement.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPVerification extends BaseEntity {
    private String email;
    private String otp;
    private LocalDateTime expiryTime;
}
