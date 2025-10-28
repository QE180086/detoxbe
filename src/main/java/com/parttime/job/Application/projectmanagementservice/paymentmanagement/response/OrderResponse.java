package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private OrderStatus status;
    private double totalAmount;
    private String userId;
    private String email;
    private List<OrderItemResponse> orderItems;
    private String address;
    private String numberPhone;
    private LocalDateTime createdDate;
    private String orderCode;
    private double shippingFee;
    private LocalDateTime expectedDeliveryTime;

}
