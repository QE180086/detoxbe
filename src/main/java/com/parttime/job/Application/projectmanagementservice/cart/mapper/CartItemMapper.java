package com.parttime.job.Application.projectmanagementservice.cart.mapper;

import com.parttime.job.Application.projectmanagementservice.cart.entity.CartItem;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "productId", source = "cartItem.product.id")
    @Mapping(target = "productName", source = "cartItem.product.name")
    @Mapping(target = "productImage", source = "cartItem.product.image")
    CartItemResponse toDTO(CartItem cartItem);
    List<CartItemResponse> toListDTO(List<CartItem> cartItems);
}
