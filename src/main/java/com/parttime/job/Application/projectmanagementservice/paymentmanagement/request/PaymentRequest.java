package com.parttime.job.Application.projectmanagementservice.paymentmanagement.request;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentRequest {
    private PaymentMethod method;
}
