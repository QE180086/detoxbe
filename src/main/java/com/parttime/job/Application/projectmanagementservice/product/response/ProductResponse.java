package com.parttime.job.Application.projectmanagementservice.product.response;

import com.parttime.job.Application.projectmanagementservice.product.entity.TypeProduct;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    double price;
    double salePrice;
    String image;
    String description;
    TypeProduct typeProduct;
    boolean isActive;
    List<RateResponse> rateResponses;
    StatisticsRate statisticsRate;
}
