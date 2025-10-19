package com.parttime.job.Application.projectmanagementservice.shippingmanagement.service;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.OrderShippingResponse;

public interface GHNService {

    String createShippingOrder(Payment payment);

    double calculateShippingFee(int toDistrictId, String toWardCode, int weight);

    String getShippingOrderStatus();

    OrderShippingResponse getDetailOrderStatusByOrderCode(String orderCode);

    String cancelOrder(String orderId);

    String updateOrder(String orderId, String toName, String toPhone, String address );
}
