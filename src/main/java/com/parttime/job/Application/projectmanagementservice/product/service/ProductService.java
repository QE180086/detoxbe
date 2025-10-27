package com.parttime.job.Application.projectmanagementservice.product.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.product.request.ProductRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductStatsResponse;

public interface ProductService {
    PagingResponse<ProductResponse> getListProduct(String searchText, PagingRequest pagingRequest);

    ProductResponse getDetailProduct(String productId);

    ProductResponse create(ProductRequest productRequest);

    ProductResponse update(String productId, ProductRequest request);

    void delete(String productId);

//    Integer countAllProduct(TypeTarget typeTarget);

    ProductStatsResponse getProductStats();
}
