package com.parttime.job.Application.projectmanagementservice.cart.mapper;

import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "cart.user.id")
    @Mapping(target = "email", source = "cart.user.email")
    CartResponse toDTO(Cart cart);
    List<CartResponse> toListDTO(List<Cart> carts);
}
