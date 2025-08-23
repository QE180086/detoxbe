package com.parttime.job.Application.projectmanagementservice.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTypeProductRequest {
    @NotBlank(message = "Id Type Product is required")
    String id;
    @NotBlank(message = "Name is required")
    String name;
    String description;
    String image;
    boolean isDeleted;
}
