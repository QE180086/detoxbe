package com.parttime.job.Application.projectmanagementservice.paymentmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.OrderItem;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "priceProduct")
    @Mapping(source = "product.salePrice", target = "salePrice")
    @Mapping(source = "product.image", target = "image")
    @Mapping(source = "product.typeProduct.name", target = "typeProductName")
    OrderItemResponse toDTO(OrderItem orderItem);
    List<OrderItemResponse> toListDTO(List<OrderItem> orderItems);
}
