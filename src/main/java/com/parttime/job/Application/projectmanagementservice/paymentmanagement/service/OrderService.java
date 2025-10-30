package com.parttime.job.Application.projectmanagementservice.paymentmanagement.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderStatsResponse;

public interface OrderService {
    PagingResponse<OrderResponse> getListOrder(String searchText, PagingRequest pagingRequest);

    PagingResponse<OrderResponse> getListOrderByUser(String userId, PagingRequest pagingRequest);

    OrderResponse getOrderDetail(String orderId);

    OrderStatus checkOrderStatus(String orderId);

    OrderStatsResponse getOrderStats();

    OrderResponse updateStatusOrder(String orderId, OrderStatus status);
}
