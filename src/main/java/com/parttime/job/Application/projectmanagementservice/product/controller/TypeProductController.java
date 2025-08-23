package com.parttime.job.Application.projectmanagementservice.product.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.product.request.ProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.request.TypeProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.request.UpdateTypeProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.response.TypeProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.service.TypeProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/type-product")
@RestController
@RequiredArgsConstructor
public class TypeProductController {
    private final TypeProductService typeProductService;

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<TypeProductResponse>>> getAllTypeProductBySearchText(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<TypeProductResponse> typeProducts = typeProductService.getAllTypeProducts(searchText, pagingRequest);

        GenericResponse<PagingResponse<TypeProductResponse>> response = GenericResponse.<PagingResponse<TypeProductResponse>>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(typeProducts)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<GenericResponse<TypeProductResponse>> createTypeProduct(@Valid @RequestBody TypeProductRequest request) {
        GenericResponse<TypeProductResponse> response = GenericResponse.<TypeProductResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(typeProductService.create(request))
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping()
    public ResponseEntity<GenericResponse<TypeProductResponse>> updateTypeProduct(@Valid @RequestBody UpdateTypeProductRequest request) {
        GenericResponse<TypeProductResponse> response = GenericResponse.<TypeProductResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(typeProductService.update(request))
                .build();
        return ResponseEntity.ok(response);
    }
}
