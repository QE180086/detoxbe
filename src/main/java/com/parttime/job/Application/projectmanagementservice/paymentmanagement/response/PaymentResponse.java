package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentMethod;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import lombok.Data;

@Data
public class PaymentResponse {

    private double amount;

    private PaymentStatus status;

    private PaymentMethod method;

    private String qrCode;

    private Orders orders;
}
