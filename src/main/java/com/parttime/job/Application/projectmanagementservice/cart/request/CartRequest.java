package com.parttime.job.Application.projectmanagementservice.cart.request;

import lombok.Data;

import java.util.List;
@Data
public class CartRequest {
    private List<CartItemRequest> items;
    private String voucherCode;
}
