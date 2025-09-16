package com.parttime.job.Application.projectmanagementservice.voucher.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.request.ExchangeVoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.request.UserVoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.request.VoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.response.UserVoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-vouchers")
@RequiredArgsConstructor
public class UserVoucherController {
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<UserVoucherResponse>>> getAllVoucherByUser(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String userId) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));
        PagingResponse<UserVoucherResponse> vouchers = voucherService.getVouchersByUser(userId, pagingRequest);
        GenericResponse<PagingResponse<UserVoucherResponse>> response = GenericResponse.<PagingResponse<UserVoucherResponse>>builder()
                .data(vouchers)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<GenericResponse<UserVoucherResponse>> receiveVoucher(@Valid @RequestBody UserVoucherRequest request) {
        UserVoucherResponse voucher = voucherService.userReceiveVoucher(request);
        GenericResponse<UserVoucherResponse> response = GenericResponse.<UserVoucherResponse>builder()
                .data(voucher)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/exchange")
    public ResponseEntity<GenericResponse<UserVoucherResponse>> exchangeVoucher(@Valid @RequestBody ExchangeVoucherRequest request) {
        UserVoucherResponse voucher = voucherService.exchangeVoucher(request);
        GenericResponse<UserVoucherResponse> response = GenericResponse.<UserVoucherResponse>builder()
                .data(voucher)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

}
