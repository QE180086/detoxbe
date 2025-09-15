package com.parttime.job.Application.projectmanagementservice.paymentmanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.PaymentRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.SepayWebhookRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .isSuccess(true)
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
                    .isSuccess(true)
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
                    .isSuccess(false)
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<GenericResponse<PaymentResponse>> updatePaymentStatus(@PathVariable String paymentId,
                                                                                @RequestParam PaymentStatus status) {
        PaymentResponse payment = paymentService.updateStatusForCashPayment(paymentId, status);
        GenericResponse<PaymentResponse> response = GenericResponse.<PaymentResponse>builder()
                .data(payment)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<PaymentResponse>>> getListOrder(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String userId
    ) {

        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<PaymentResponse> payments = paymentService.getListPayment(userId, pagingRequest);
        GenericResponse<PagingResponse<PaymentResponse>> response = GenericResponse.<PagingResponse<PaymentResponse>>builder()
                .data(payments)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
