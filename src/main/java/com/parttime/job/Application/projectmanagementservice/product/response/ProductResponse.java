package com.parttime.job.Application.projectmanagementservice.product.response;

import com.parttime.job.Application.projectmanagementservice.product.entity.TypeProduct;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    BigDecimal price;
    BigDecimal salePrice;
    String image;
    String description;
    TypeProduct typeProduct;
    boolean isActive;

}
