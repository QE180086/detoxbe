package com.parttime.job.Application.projectmanagementservice.cart.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.cart.request.CartItemRequest;
import com.parttime.job.Application.projectmanagementservice.cart.request.UpdateCartItemRequest;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartItemResponse;

public interface CartItemService {
    CartItemResponse addCartItem(CartItemRequest request);

    PagingResponse<CartItemResponse> getCartItemsByCartId(PagingRequest pagingRequest);

    CartItemResponse getCartItemById(String cartItemId);

    CartItemResponse updateCartItem(UpdateCartItemRequest request);

    void removeCartItem(String cartItemId);

    void removeAllItemsFromCart();
}
