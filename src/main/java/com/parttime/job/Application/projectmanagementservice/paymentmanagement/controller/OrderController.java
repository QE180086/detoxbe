package com.parttime.job.Application.projectmanagementservice.paymentmanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.OrderStatsResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<OrderResponse>>> getListOrder(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText
    ) {

        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<OrderResponse> orders = orderService.getListOrder(searchText, pagingRequest);
        GenericResponse<PagingResponse<OrderResponse>> response = GenericResponse.<PagingResponse<OrderResponse>>builder()
                .data(orders)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/user")
    public ResponseEntity<GenericResponse<PagingResponse<OrderResponse>>> getListOrderByUser(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String userId
    ) {

        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<OrderResponse> orders = orderService.getListOrderByUser(userId, pagingRequest);
        GenericResponse<PagingResponse<OrderResponse>> response = GenericResponse.<PagingResponse<OrderResponse>>builder()
                .data(orders)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GenericResponse<OrderResponse>> getOrderByOrderId(@PathVariable String orderId) {
        OrderResponse order = orderService.getOrderDetail(orderId);
        GenericResponse<OrderResponse> response = GenericResponse.<OrderResponse>builder()
                .data(order)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<GenericResponse<OrderStatus>> checkStatusOrder(@RequestParam String orderId) {
        GenericResponse<OrderStatus> response = GenericResponse.<OrderStatus>builder()
                .data(orderService.checkOrderStatus(orderId))
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/completed-percent")
    public ResponseEntity<GenericResponse<OrderStatsResponse>> getCompletedOrderPercent() {
        GenericResponse<OrderStatsResponse> response = GenericResponse.<OrderStatsResponse>builder()
                .data(orderService.getOrderStats())
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<GenericResponse<OrderResponse>> updateOrderStatus(@PathVariable String orderId,
                                                                                @RequestParam OrderStatus status) {
        OrderResponse orderResponse = orderService.updateStatusOrder(orderId, status);
        GenericResponse<OrderResponse> response = GenericResponse.<OrderResponse>builder()
                .data(orderResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
