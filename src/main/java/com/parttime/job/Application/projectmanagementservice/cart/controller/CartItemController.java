package com.parttime.job.Application.projectmanagementservice.cart.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.cart.request.CartItemRequest;
import com.parttime.job.Application.projectmanagementservice.cart.request.UpdateCartItemRequest;
import com.parttime.job.Application.projectmanagementservice.cart.response.CartItemResponse;
import com.parttime.job.Application.projectmanagementservice.cart.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-item")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;


    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<CartItemResponse>>> getAllCartItemInCart  (
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction) {

        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<CartItemResponse> cartItems = cartItemService.getCartItemsByCartId( pagingRequest);
        GenericResponse<PagingResponse<CartItemResponse>> response = GenericResponse.<PagingResponse<CartItemResponse>>builder()
                .data(cartItems)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<GenericResponse<CartItemResponse>> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {

        CartItemResponse cartItem = cartItemService.addCartItem(cartItemRequest);
        GenericResponse<CartItemResponse> response = GenericResponse.<CartItemResponse>builder()
                .data(cartItem)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/detail")
    public ResponseEntity<GenericResponse<CartItemResponse>> getDetailCartItem(@RequestParam String cartItemId) {
        CartItemResponse cartItem = cartItemService.getCartItemById(cartItemId);
        GenericResponse<CartItemResponse> response = GenericResponse.<CartItemResponse>builder()
                .data(cartItem)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<GenericResponse<CartItemResponse>> updateCartItem(@Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        CartItemResponse cartItem = cartItemService.updateCartItem(updateCartItemRequest);
        GenericResponse<CartItemResponse> response = GenericResponse.<CartItemResponse>builder()
                .data(cartItem)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<GenericResponse<Void>> deleteItemFromCart(@RequestParam String cartItemId) {
       cartItemService.removeCartItem(cartItemId);
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.DELETE_DATA_SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/all")
    public ResponseEntity<GenericResponse<Void>> deleteAllItemFromCart() {
        cartItemService.removeAllItemsFromCart();
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
