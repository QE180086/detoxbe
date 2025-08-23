package com.parttime.job.Application.projectmanagementservice.cart.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartResponse;
import com.parttime.job.Application.projectmanagementservice.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<CartResponse>>> getAllCart(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText
    ) {

        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<CartResponse> carts = cartService.getListCart(searchText, pagingRequest);
        GenericResponse<PagingResponse<CartResponse>> response = GenericResponse.<PagingResponse<CartResponse>>builder()
                .data(carts)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<GenericResponse<CartResponse>> createCart() {
        CartResponse cart = cartService.getOrCreateCartByUserId();
        GenericResponse<CartResponse> response = GenericResponse.<CartResponse>builder()
                .data(cart)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<GenericResponse<CartResponse>> getCartByCartId(@PathVariable String cartId) {
        CartResponse cart = cartService.getCartByCartId(cartId);
        GenericResponse<CartResponse> response = GenericResponse.<CartResponse>builder()
                .data(cart)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/apply-voucher")
    public ResponseEntity<GenericResponse<CartResponse>> applyVoucherCode(@RequestParam String voucherCode) {
        CartResponse cart = cartService.applyVoucherToCart(voucherCode);
        GenericResponse<CartResponse> response = GenericResponse.<CartResponse>builder()
                .data(cart)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }
}
