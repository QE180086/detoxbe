package com.parttime.job.Application.projectmanagementservice.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RateRequest {
    @NotBlank(message = "Product ID cannot be blank")
    private String productId;
    @Min(value = 1, message = "Rating must be at least 1")
    private int rateValue;

    private String comment;

}
