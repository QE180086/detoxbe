package com.parttime.job.Application.projectmanagementservice.usermanagement.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserStatsResponse {
    private Long totalUsers;
    private Double todayIncreasePercent;
}
