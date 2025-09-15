package com.parttime.job.Application.projectmanagementservice.product.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.projectmanagementservice.product.request.RateRequest;
import com.parttime.job.Application.projectmanagementservice.product.request.UpdateRateRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.RateResponse;
import com.parttime.job.Application.projectmanagementservice.product.service.RateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/rate")
@RestController
@RequiredArgsConstructor
public class RateController {
    private final RateService rateService;

    @PostMapping
    public ResponseEntity<GenericResponse<RateResponse>> createRate(@Valid @RequestBody RateRequest request) {
        GenericResponse<RateResponse> response = GenericResponse.<RateResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(rateService.createRate(request))
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping
    public ResponseEntity<GenericResponse<RateResponse>> updateRate(@Valid @RequestBody UpdateRateRequest request) {
        GenericResponse<RateResponse> response = GenericResponse.<RateResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(rateService.updateRate(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteRate(@PathVariable("id") String rateId) {
        rateService.deleteRate(rateId);
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.DELETE_DATA_SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
