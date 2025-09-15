package com.parttime.job.Application.projectmanagementservice.product.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.product.request.ProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/product")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @PostMapping
    public ResponseEntity<GenericResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        GenericResponse<ProductResponse> response = GenericResponse.<ProductResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(productService.create(request))
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<GenericResponse<ProductResponse>> updateProduct(@PathVariable String productId,
                                                                               @Valid @RequestBody ProductRequest request) {
        GenericResponse<ProductResponse> response = GenericResponse.<ProductResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(productService.update(productId, request))
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/count-combo")
//    public ResponseEntity<GenericResponse<Integer>> countComboBooking(@RequestParam(required = false) TypeTarget typeTarget) {
//        GenericResponse<Integer> response = GenericResponse.<Integer>builder()
//                .message(MessageDTO.builder()
//                        .messageCode(MessageCodeConstant.M001_SUCCESS)
//                        .messageDetail(MessageConstant.SUCCESS)
//                        .build())
//                .isSuccess(true)
//                .data(productService.countAllComboBooking(typeTarget))
//                .build();
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{productId}")
    public ResponseEntity<GenericResponse<ProductResponse>> getDetailProduct(@PathVariable String productId) {
        GenericResponse<ProductResponse> response = GenericResponse.<ProductResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(productService.getDetailProduct(productId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<ProductResponse>>> getAllProductBySearchText(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<ProductResponse> comboBooking = productService.getListProduct(searchText, pagingRequest);

        GenericResponse<PagingResponse<ProductResponse>> response = GenericResponse.<PagingResponse<ProductResponse>>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(comboBooking)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteProduct(@PathVariable("id") String productId) {
        productService.delete(productId);
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
