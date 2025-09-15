package com.parttime.job.Application.projectmanagementservice.usermanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.profile.service.ProfileService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.OTPConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.OTPVerification;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.OTPVerificationRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.EmailService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.templet.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final OTPVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;
    @Value("${spring.mail.username}")
    private String email;

    @Transactional
    @Async
    @Override
    public void sendOTP(String recipient) throws MessagingException {
        Optional<OTPVerification> otpVerification = otpVerificationRepository.findByEmail(recipient);
        if (otpVerification.isPresent()) {
            otpVerificationRepository.deleteByEmail(recipient);
        }
        String otp = generateOTPEmail();
        OTPVerification verification = new OTPVerification();
        verification.setEmail(recipient);
        verification.setOtp(otp);
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String html = EmailTemplate.VERIFICATION_CODE_EMAIL.getBody(otp.toString());

        helper.setTo(recipient);
        helper.setSubject(EmailTemplate.VERIFICATION_CODE_EMAIL.getSubject());
        helper.setText(html, true);
        helper.setFrom(email);

        javaMailSender.send(message);
        otpVerificationRepository.save(verification);
    }

    @Override
    @Transactional
    public boolean verifyOtp(String recipient, String otp) {
        Optional<OTPVerification> otpVerification = otpVerificationRepository.findByEmail(recipient);
        if (otpVerification.isEmpty()) {
            throw new AppException(MessageCodeConstant.M005_INVALID, OTPConstant.OTP_NOT_FOUND);
        }
        if (otpVerification.get().getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new AppException(MessageCodeConstant.M005_INVALID, OTPConstant.OTP_EXPIRED_TIME);
        }

        if (!otpVerification.get().getOtp().equals(otp)) {
            throw new AppException(MessageCodeConstant.M005_INVALID, OTPConstant.OTP_NOT_VERIFY);
        }
        otpVerificationRepository.deleteByEmail(recipient);
        Optional<User> user = userRepository.findByEmail(recipient);
        user.get().setStatus(UserStatus.ACTIVE);
        userRepository.save(user.get());
        return true;
    }

    private String generateOTPEmail() {
        return String.format("%06d", new Random().nextInt(999999));
    }

}
