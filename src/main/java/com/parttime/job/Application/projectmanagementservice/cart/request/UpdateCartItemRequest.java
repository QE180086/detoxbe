package com.parttime.job.Application.projectmanagementservice.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotBlank(message = "CartItem ID cannot be blank")
    private String cartItemId;
}
