package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueCompareResponse {
    private Double totalRevenue;
    private Double percentChange;
}
