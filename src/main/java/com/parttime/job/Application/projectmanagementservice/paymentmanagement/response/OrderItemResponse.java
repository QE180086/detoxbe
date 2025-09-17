package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private String priceProduct;
    private String salePrice;
    private String productName;
    private String image;
    private String typeProductName;

    private int quantity;
    private double price;
}
