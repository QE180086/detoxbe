package com.parttime.job.Application.projectmanagementservice.paymentmanagement.request;

import lombok.Data;

@Data
public class SepayWebhookRequest {
    private String transaction_id;
    private double amount;
    private String description;
    private String account_number;
    private String transaction_time;
}
