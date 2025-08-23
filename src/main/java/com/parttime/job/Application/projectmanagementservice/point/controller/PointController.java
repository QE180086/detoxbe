package com.parttime.job.Application.projectmanagementservice.point.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartResponse;
import com.parttime.job.Application.projectmanagementservice.point.request.PointRequest;
import com.parttime.job.Application.projectmanagementservice.point.response.PointResponse;
import com.parttime.job.Application.projectmanagementservice.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {
    private PointService pointService;
    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<PointResponse>>> getAllPoint(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText
    ) {

        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<PointResponse> points = pointService.getListPoint(searchText, pagingRequest);
        GenericResponse<PagingResponse<PointResponse>> response = GenericResponse.<PagingResponse<PointResponse>>builder()
                .data(points)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-point")
    public ResponseEntity<GenericResponse<PointResponse>> addPoint(@Valid @RequestBody PointRequest request) {
        PointResponse point = pointService.addPoint(request);
        GenericResponse<PointResponse> response = GenericResponse.<PointResponse>builder()
                .data(point)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/minus-point")
    public ResponseEntity<GenericResponse<PointResponse>> minusPoint(@Valid @RequestBody PointRequest request) {
        PointResponse point = pointService.minusPoint(request);
        GenericResponse<PointResponse> response = GenericResponse.<PointResponse>builder()
                .data(point)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail")
    public ResponseEntity<GenericResponse<PointResponse>> getPointById() {
        PointResponse point = pointService.getPointByUserId();
        GenericResponse<PointResponse> response = GenericResponse.<PointResponse>builder()
                .data(point)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }
}
