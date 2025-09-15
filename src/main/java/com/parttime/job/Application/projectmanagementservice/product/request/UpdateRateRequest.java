package com.parttime.job.Application.projectmanagementservice.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRateRequest {
    @NotBlank(message = "Rate ID cannot be blank")
    private String rateId;
    @Min(value = 1, message = "Rating must be at least 1")
    private int rateValue;
    private String comment;
}
