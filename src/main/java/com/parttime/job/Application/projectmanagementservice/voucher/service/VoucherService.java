package com.parttime.job.Application.projectmanagementservice.voucher.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.request.UserVoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.request.VoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.response.UserVoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;

public interface VoucherService {
    PagingResponse<VoucherResponse> getAllVouchers(String searchText, PagingRequest request);

    VoucherResponse create(VoucherRequest request);

    VoucherResponse getVoucherById(String voucherId);

    VoucherResponse updateVoucher(String voucherId, VoucherRequest request);

    void deleteVoucher(String voucherId);

//    VoucherResponse applyVoucherToCart(String voucherCode);

    UserVoucherResponse userReceiveVoucher(UserVoucherRequest request);

    PagingResponse<UserVoucherResponse> getVouchersByUser(String userId, PagingRequest request);

    UserVoucherResponse exchangeVoucher(String voucherId);
}
