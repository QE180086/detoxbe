package com.parttime.job.Application.projectmanagementservice.usermanagement.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOTP(String recipient) throws MessagingException;
    boolean verifyOtp(String recipient, String otp);
}
