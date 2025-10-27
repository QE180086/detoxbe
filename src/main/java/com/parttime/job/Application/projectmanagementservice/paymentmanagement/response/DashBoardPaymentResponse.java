package com.parttime.job.Application.projectmanagementservice.paymentmanagement.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardPaymentResponse {
    private LocalDate date;
    private Double revenue;
}
