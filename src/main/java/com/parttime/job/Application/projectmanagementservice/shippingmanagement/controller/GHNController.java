package com.parttime.job.Application.projectmanagementservice.shippingmanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.OrderShippingResponse;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.service.GHNService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ghn")
@RequiredArgsConstructor
public class GHNController {
    private final GHNService ghnService;

    @PostMapping("/order-detail")
    public ResponseEntity<GenericResponse<OrderShippingResponse>> getOrderByOrderCode(@RequestParam String order_code) {
        OrderShippingResponse order = ghnService.getDetailOrderStatusByOrderCode(order_code);
        GenericResponse<OrderShippingResponse> response = GenericResponse.<OrderShippingResponse>builder()
                .data(order)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel-order")
    public ResponseEntity<GenericResponse<String>> cancelOrderByOrderCode(@RequestParam String orderId) {
        String order = ghnService.cancelOrder(orderId);
        GenericResponse<String> response = GenericResponse.<String>builder()
                .data(order)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-order")
    public ResponseEntity<GenericResponse<String>> updateOrderByOrderCode(@RequestParam String orderId,
                                                                          @RequestParam String toName,
                                                                          @RequestParam String toPhone,
                                                                          @RequestParam String address) {
        String order = ghnService.updateOrder(orderId, toName, toPhone, address);
        GenericResponse<String> response = GenericResponse.<String>builder()
                .data(order)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
