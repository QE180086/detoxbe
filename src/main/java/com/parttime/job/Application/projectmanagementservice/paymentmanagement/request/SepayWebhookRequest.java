package com.parttime.job.Application.projectmanagementservice.paymentmanagement.request;

import lombok.Data;

@Data
public class SepayWebhookRequest {
    private String id;
    private double transferAmount;
    private String description;
    private String accountNumber;
    private String transactionDate;
}
