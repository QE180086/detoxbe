package com.parttime.job.Application.projectmanagementservice.cart.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotBlank(message = "Product ID is required")
    private String productId;
}
