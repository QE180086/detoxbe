package com.parttime.job.Application.projectmanagementservice.voucher.response;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import lombok.Data;

@Data
public class UserVoucherResponse {
    private UserResponse user;
    private VoucherResponse voucher;
    private boolean used;
}
