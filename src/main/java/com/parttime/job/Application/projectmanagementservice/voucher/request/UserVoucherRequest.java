package com.parttime.job.Application.projectmanagementservice.voucher.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserVoucherRequest {
    @NotBlank(message = "User Id cannot be blank")
    private String userId;
    @NotBlank(message = "Voucher Id cannot be blank")
    private String voucherId;
}
