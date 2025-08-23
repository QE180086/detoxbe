package com.parttime.job.Application.projectmanagementservice.usermanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.SendOTPRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UserRequestLogin;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UserRequestRegister;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.VerifyOtpRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.AuthResponseLogin;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.AuthenticationService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity<GenericResponse<AuthResponseLogin>> login(@Valid @RequestBody UserRequestLogin request) {
        GenericResponse<AuthResponseLogin> response = GenericResponse.<AuthResponseLogin>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(authenticationService.login(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<UserResponse>> register(@Valid @RequestBody UserRequestRegister request) throws MessagingException {
        GenericResponse<UserResponse> response = GenericResponse.<UserResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(authenticationService.register(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<GenericResponse<Void>> sendOTP(@Valid @RequestBody SendOTPRequest request) throws MessagingException {
        emailService.sendOTP(request.getEmail());
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<GenericResponse<Void>> verifyOTP(@Valid @RequestBody VerifyOtpRequest request) throws MessagingException {
        emailService.verifyOtp(request.getEmail(), request.getOtp());
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }



}
