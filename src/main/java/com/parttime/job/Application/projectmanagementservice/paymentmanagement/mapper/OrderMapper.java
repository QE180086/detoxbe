package com.parttime.job.Application.projectmanagementservice.paymentmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface OrderMapper {
    OrderResponse toDTO(Orders orders);
    List<OrderResponse> toListDTO(List<Orders> orders);
}
