package com.parttime.job.Application.projectmanagementservice.cart.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private String id;
    private String productId;
    private String productName;
    private String productImage;
    private double unitPrice;
    private int quantity;
    private double totalPrice;
}
