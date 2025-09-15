package com.parttime.job.Application.projectmanagementservice.voucher.response;

import lombok.Data;

import java.util.Date;

@Data
public class VoucherResponse {
    private String id;
    private String code;
    private double discountValue;
    private boolean isPercentage;
    private double minOrderValue;
    private boolean isActive;
    private Date createdDate;
    private String image;
    private int exchangePoint;

}
