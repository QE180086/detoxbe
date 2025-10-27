package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatsResponse {
    private Long totalCompletedOrders;
    private Double todayIncreasePercent;
}
