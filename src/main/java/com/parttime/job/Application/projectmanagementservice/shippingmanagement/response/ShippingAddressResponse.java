package com.parttime.job.Application.projectmanagementservice.shippingmanagement.response;

import lombok.Data;

@Data
public class ShippingAddressResponse {
    private String id;
    private String note;
    private String receiverName;
    private String phoneNumber;
    private String addressLine;
}
