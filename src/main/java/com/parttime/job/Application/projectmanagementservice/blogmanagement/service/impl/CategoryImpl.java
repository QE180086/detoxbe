package com.parttime.job.Application.projectmanagementservice.blogmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.constant.MessageConstantBlog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.CategoryRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CategoryResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Category;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.mapper.CategoryMapper;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.repository.CategoryRepository;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final int PAGE_SIZE_INDEX = 1;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        List<String> nameCategory = categoryRepository.findNameExistCategory();
        for (String name : nameCategory) {
            if (categoryRequest.getName().trim().equals(name)) {
                throw new AppException(MessageCodeConstant.M004_DUPLICATE, MessageConstantBlog.DATA_IS_EXIST);
            }
        }
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setIsActive(true);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.CATEGORY_NOT_FOUND);
        }
        if (categoryRequest.getName() != null && !categoryRequest.getName().trim().isEmpty()) {
            category.get().setName(categoryRequest.getName());
        }

        if (categoryRequest.isActive() != Boolean.TRUE.equals(category.get().getIsActive())) {
            category.get().setIsActive(categoryRequest.isActive());
        }
        categoryRepository.save(category.get());
        return categoryMapper.toCategoryResponse(category.get());
    }

    @Override
    public PagingResponse<CategoryResponse> getCategoryAndSearch(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Category> categoryPage = categoryRepository.getAllCategoryAndSearchText(searchText, pageRequest);
        if (categoryPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        List<CategoryResponse> categoryResponse = categoryMapper.toListCategoryResponse(categoryPage.getContent());
        return new PagingResponse<>(categoryResponse, pagingRequest, categoryPage.getTotalElements());

    }

    @Override
    public PagingResponse<CategoryResponse> getCategoryBySearchText(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Category> categoryPage = categoryRepository.getCategoryBySearchText(searchText, pageRequest);
        if (categoryPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        List<CategoryResponse> categoryResponse = categoryMapper.toListCategoryResponse(categoryPage.getContent());
        return new PagingResponse<>(categoryResponse, pagingRequest, categoryPage.getTotalElements());
    }

    @Override
    public List<CategoryResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toListCategoryResponse(categories);
    }


}
