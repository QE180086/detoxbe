package com.parttime.job.Application.projectmanagementservice.cart.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartResponse;

public interface CartService {
    CartResponse getOrCreateCartByUserId();

    PagingResponse<CartResponse> getListCart(String searchText,PagingRequest request);

    CartResponse getCartByCartId(String cartId);

    CartResponse applyVoucherToCart(String voucherCode);

    CartResponse removeVoucherFromCart();

}
