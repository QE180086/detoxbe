package com.parttime.job.Application.projectmanagementservice.shippingmanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.repository.CartRepository;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.AddressInfo;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.response.OrderShippingResponse;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.service.ChangeAddressInfo;
import com.parttime.job.Application.projectmanagementservice.shippingmanagement.service.GHNService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/ghn")
@RequiredArgsConstructor
public class GHNController {
    private final GHNService ghnService;
    private final ChangeAddressInfo changeAddressInfo;
    private final CartRepository cartRepository;

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

    @PostMapping("/calculate-fee")
    public ResponseEntity<GenericResponse<Double>> calculateFee(@RequestParam String fullAddress,
                                                                @RequestParam String cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Cart not found");
        }
        AddressInfo fee = changeAddressInfo.resolveAddress(fullAddress);
        double shippingFee = ghnService.calculateShippingFee(fee.getDistrictId(), fee.getWardCode(), cart.get().getCartItems().size()   * 348);
        GenericResponse<Double> response = GenericResponse.<Double>builder()
                .data(shippingFee)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }
}
