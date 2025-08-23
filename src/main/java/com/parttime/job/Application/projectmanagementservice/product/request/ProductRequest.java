package com.parttime.job.Application.projectmanagementservice.product.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name must not be blank")
    @Size(max = 100, message = "Product name must be at most 100 characters")
    String name;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0")
    double price;

    @NotNull(message = "Sale price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Sale price must be greater than or equal to 0")
    double salePrice;

    @NotBlank(message = "Product image URL must not be blank")
    String image;

    @NotBlank(message = "Product description must not be blank")
    @Size(max = 2000, message = "Product description must be at most 2000 characters")
    String description;

    @NotNull(message = "Product active status is required")
    boolean isActive;

    @NotBlank(message = "Type product ID must not be blank")
    String typeProductId;


}
