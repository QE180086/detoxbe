package com.parttime.job.Application.projectmanagementservice.product.mapper;

import com.parttime.job.Application.projectmanagementservice.product.entity.TypeProduct;
import com.parttime.job.Application.projectmanagementservice.product.response.TypeProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TypeProductMapper {
    TypeProductResponse toDTO(TypeProduct typeProduct);
    List<TypeProductResponse> toDTOs(List<TypeProduct> typeProducts);
}
