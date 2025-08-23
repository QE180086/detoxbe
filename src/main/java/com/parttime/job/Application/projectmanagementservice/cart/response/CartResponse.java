package com.parttime.job.Application.projectmanagementservice.cart.response;

import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import lombok.Data;

import java.util.List;

@Data

public class CartResponse {
    private String id;
    private String userId;
    private String email;
    private List<CartItemResponse> cartItems;
    private Double totalPrice;
    private Double discountedPrice;
    private String appliedVoucher;
    private VoucherResponse voucherResponse;
}
