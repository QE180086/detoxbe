package com.parttime.job.Application.projectmanagementservice.usermanagement.repository;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.OTPVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPVerificationRepository extends JpaRepository<OTPVerification, String> {
    Optional<OTPVerification> findByEmail(String email);
    void deleteByEmail(String email);
}
