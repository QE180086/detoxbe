package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.OrderItem;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private OrderStatus status;
    private double totalAmount;
    private User user;
    private List<OrderItem> orderItems;
    private String address;
    private String numberPhone;
}
