package com.parttime.job.Application.projectmanagementservice.blogmanagement.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.CategoryRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(CategoryRequest categoryRequest, String categoryId);
    PagingResponse<CategoryResponse> getCategoryAndSearch(String searchText, PagingRequest pagingRequest);
    PagingResponse<CategoryResponse> getCategoryBySearchText(String searchText, PagingRequest pagingRequest);
    List<CategoryResponse> getAllCategory();
}
