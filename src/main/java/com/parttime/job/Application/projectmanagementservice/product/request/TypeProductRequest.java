package com.parttime.job.Application.projectmanagementservice.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TypeProductRequest {
    @NotBlank(message = "Name is required")
    String name;
    String description;
    String image;
}
