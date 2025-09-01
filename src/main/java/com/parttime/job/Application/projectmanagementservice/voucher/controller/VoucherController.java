package com.parttime.job.Application.projectmanagementservice.voucher.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.request.VoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<VoucherResponse>>> getAllVoucher(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<VoucherResponse> vouchers = voucherService.getAllVouchers(searchText, pagingRequest);
        GenericResponse<PagingResponse<VoucherResponse>> response = GenericResponse.<PagingResponse<VoucherResponse>>builder()
                .data(vouchers)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<GenericResponse<VoucherResponse>> getVoucherDetail(@PathVariable String voucherId) {

        VoucherResponse vouchers = voucherService.getVoucherById(voucherId);
        GenericResponse<VoucherResponse> response = GenericResponse.<VoucherResponse>builder()
                .data(vouchers)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{voucherId}")
    public ResponseEntity<GenericResponse<VoucherResponse>> updateVoucher(@PathVariable String voucherId, @Valid @RequestBody VoucherRequest request) {

        VoucherResponse vouchers = voucherService.updateVoucher(voucherId, request);
        GenericResponse<VoucherResponse> response = GenericResponse.<VoucherResponse>builder()
                .data(vouchers)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<GenericResponse<VoucherResponse>> createVoucher(@Valid @RequestBody VoucherRequest request) {
        VoucherResponse voucher = voucherService.create(request);
        GenericResponse<VoucherResponse> response = GenericResponse.<VoucherResponse>builder()
                .data(voucher)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }


//    @DeleteMapping("/{voucherId}")
//    public ResponseEntity<GenericResponse<VoucherResponse>> updateVoucher(@PathVariable String voucherId, @Valid @RequestBody VoucherRequest request) {
//
//        VoucherResponse vouchers = voucherService.updateVoucher(voucherId, request);
//        GenericResponse<VoucherResponse> response = GenericResponse.<VoucherResponse>builder()
//                .data(vouchers)
//                .message(MessageDTO.builder()
//                        .messageCode(MessageCodeConstant.M001_SUCCESS)
//                        .messageDetail(MessageConstant.SUCCESS)
//                        .build())
//                .build();
//        return ResponseEntity.ok(response);
//
//    }

}
