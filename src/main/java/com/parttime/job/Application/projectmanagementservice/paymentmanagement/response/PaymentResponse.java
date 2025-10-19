package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentMethod;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private double amount;

    private PaymentStatus status;

    private PaymentMethod method;

    private String qrCode;

    private String ordersId;

    private double shippingFee;

    private double amountNotFee;

    private String orderCode;

    private LocalDateTime expectedDeliveryTime;

}
