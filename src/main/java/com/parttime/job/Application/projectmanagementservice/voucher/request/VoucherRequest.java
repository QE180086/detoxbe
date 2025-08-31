package com.parttime.job.Application.projectmanagementservice.voucher.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VoucherRequest {
    @NotBlank(message = "Code cannot be blank")
    private String code;
    @Min(value = 0, message = "Discount value must be greater than or equal to 0")
    private double discountValue;
    private boolean isPercentage;
    @Min(value = 0, message = "Minimum order value must be >= 0")
    private double minOrderValue;
    private boolean isActive;
    private String image;
    private int exchangePoint;

}
