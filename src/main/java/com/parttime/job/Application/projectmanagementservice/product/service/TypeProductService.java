package com.parttime.job.Application.projectmanagementservice.product.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.product.request.TypeProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.request.UpdateTypeProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.TypeProductResponse;

public interface TypeProductService {
    TypeProductResponse create(TypeProductRequest request);
    TypeProductResponse update(UpdateTypeProductRequest request);
    PagingResponse<TypeProductResponse> getAllTypeProducts(String searchText, PagingRequest request);
}
