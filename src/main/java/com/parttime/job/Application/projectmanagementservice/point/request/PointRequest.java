package com.parttime.job.Application.projectmanagementservice.point.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PointRequest {
    @Min(value = 0, message = "Amount must be a positive integer")
    private int amount;
}
