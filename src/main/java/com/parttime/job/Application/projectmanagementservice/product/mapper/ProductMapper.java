package com.parttime.job.Application.projectmanagementservice.product.mapper;

import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toDTO(Product product);

    List<ProductResponse> toListDTO(List<Product> product);
}
