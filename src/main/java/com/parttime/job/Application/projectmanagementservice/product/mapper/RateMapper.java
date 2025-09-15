package com.parttime.job.Application.projectmanagementservice.product.mapper;

import com.parttime.job.Application.projectmanagementservice.product.entity.Rate;
import com.parttime.job.Application.projectmanagementservice.product.response.RateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateMapper {
    @Mapping(target = "productId", source = "rate.product.id")
    @Mapping(target = "productName", source = "rate.product.name")
    @Mapping(target = "fullName", source = "rate.user.profile.fullName")
    @Mapping(target = "avatar", source = "rate.user.profile.avatar")
    RateResponse toDTO(Rate rate);
    List<RateResponse> toListDTO(List<Rate> rates);
}
