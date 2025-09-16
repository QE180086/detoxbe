package com.parttime.job.Application.projectmanagementservice.blogmanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.CategoryRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CategoryResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blog-categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<GenericResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        GenericResponse<CategoryResponse> response = GenericResponse.<CategoryResponse>builder()
                .data(categoryResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<CategoryResponse>> updateCategory(@PathVariable("id") String categoryId,
                                                                            @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.updateCategory(categoryRequest, categoryId);
        GenericResponse<CategoryResponse> response = GenericResponse.<CategoryResponse>builder()
                .data(categoryResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/search-category-name")
    public ResponseEntity<GenericResponse<PagingResponse<CategoryResponse>>> getAllCategorySearchByName(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<CategoryResponse> categoryResponse = categoryService.getCategoryAndSearch(searchText, pagingRequest);
        GenericResponse<PagingResponse<CategoryResponse>> response = GenericResponse.<PagingResponse<CategoryResponse>>builder()
                .data(categoryResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/search")
    public ResponseEntity<GenericResponse<PagingResponse<CategoryResponse>>> getAllCategoryByTitleBlog(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<CategoryResponse> categoryResponse = categoryService.getCategoryBySearchText(searchText, pagingRequest);
        GenericResponse<PagingResponse<CategoryResponse>> response = GenericResponse.<PagingResponse<CategoryResponse>>builder()
                .data(categoryResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<CategoryResponse>>> getAllCategory() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategory();
        GenericResponse<List<CategoryResponse>> response = GenericResponse.<List<CategoryResponse>>builder()
                .data(categoryResponses)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

}
