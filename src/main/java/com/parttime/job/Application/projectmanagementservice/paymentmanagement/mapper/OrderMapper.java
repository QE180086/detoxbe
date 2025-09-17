package com.parttime.job.Application.projectmanagementservice.paymentmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})

public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "orderStatus", target = "status")
    OrderResponse toDTO(Orders orders);
    List<OrderResponse> toListDTO(List<Orders> orders);
}
