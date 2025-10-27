package com.parttime.job.Application.projectmanagementservice.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatsResponse {
    private Long totalProducts;
    private Double todayIncreasePercent;
}
