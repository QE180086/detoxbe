package com.parttime.job.Application.projectmanagementservice.paymentmanagement.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.PaymentRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.request.SepayWebhookRequest;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest);

    PaymentResponse handleWebhook(SepayWebhookRequest request);

    PaymentResponse updateStatusForCashPayment(String paymentId, PaymentStatus paymentStatus);

    PagingResponse<PaymentResponse> getListPayment(String userId, PagingRequest pagingRequest);

}
