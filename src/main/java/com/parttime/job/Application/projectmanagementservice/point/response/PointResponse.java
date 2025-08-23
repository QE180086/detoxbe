package com.parttime.job.Application.projectmanagementservice.point.response;

import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import lombok.Data;

import java.util.List;

@Data
public class PointResponse {
    private String id;
    private String userId;
    private String email;
    private int currentPoints;
    private List<VoucherResponse> voucherResponses;
}
