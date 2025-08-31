package com.parttime.job.Application.projectmanagementservice.paymentmanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.PaymentRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.SepayWebhookRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<GenericResponse<PaymentResponse>> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse payment = paymentService.createPayment(request);
        GenericResponse<PaymentResponse> response = GenericResponse.<PaymentResponse>builder()
                .data(payment)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<GenericResponse<PaymentResponse>> handleWebhook(@RequestBody SepayWebhookRequest request) {
        try {
            GenericResponse<PaymentResponse> response = GenericResponse.<PaymentResponse>builder()
                    .data(paymentService.handleWebhook(request))
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M001_SUCCESS)
                            .messageDetail(MessageConstant.SUCCESS)
                            .build())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            GenericResponse<PaymentResponse> response = GenericResponse.<PaymentResponse>builder()
                    .data(null)
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M026_FAIL)
                            .messageDetail(e.getMessage())
                            .build())
                    .build();
            return ResponseEntity.ok(response);
        }
    }
}
