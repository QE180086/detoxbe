package com.parttime.job.Application.projectmanagementservice.blogmanagement.controller;


import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.ValidationUtil;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.constant.MessageConstantBlog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.*;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogInteractiveResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<BlogResponse>>> getAllBlog(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<BlogResponse> blogs = blogService.getAllBlog(searchText, pagingRequest);
        GenericResponse<PagingResponse<BlogResponse>> response = GenericResponse.<PagingResponse<BlogResponse>>builder()
                .data(blogs)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{slugName}")
    public ResponseEntity<GenericResponse<BlogResponse>> getBlogDetail(@PathVariable String slugName) {

        BlogResponse blogs = blogService.findBlogBySlugName(slugName);
        GenericResponse<BlogResponse> response = GenericResponse.<BlogResponse>builder()
                .data(blogs)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/search")
    public ResponseEntity<GenericResponse<PagingResponse<BlogResponse>>> searchBlogByField(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String category) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));
        if (!ValidationUtil.isFieldExist(Blog.class, field)) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstantBlog.INVALID_FIELD_SORT);
        }
        SearchBlogByField searchBlogByField = new SearchBlogByField();
        searchBlogByField.setTitle(title);
        searchBlogByField.setContent(content);
        searchBlogByField.setCategory(category);
        PagingResponse<BlogResponse> blogs = blogService.searchBlog(searchBlogByField, pagingRequest);
        GenericResponse<PagingResponse<BlogResponse>> response = GenericResponse.<PagingResponse<BlogResponse>>builder()
                .data(blogs)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<GenericResponse<BlogResponse>> createBlog(@Valid @RequestBody CreateBlogRequest request) {
        BlogResponse blog = blogService.createBlog(request);
        GenericResponse<BlogResponse> response = GenericResponse.<BlogResponse>builder()
                .data(blog)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{slugName}")
    public ResponseEntity<GenericResponse<BlogResponse>> updateBlog(@PathVariable("slugName") String slugName,
                                                                    @RequestBody(required = false) UpdateRequestBlog request) {
        BlogResponse blog = blogService.updateBlog(request, slugName);
        GenericResponse<BlogResponse> response = GenericResponse.<BlogResponse>builder()
                .data(blog)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/show/{slugName}")
    public ResponseEntity<GenericResponse<BlogResponse>> updateBlogShow(@PathVariable String slugName,
                                                                        @RequestBody BlogShowRequest show) {
        BlogResponse blog = blogService.updateBlogShow(slugName, show);
        GenericResponse<BlogResponse> response = GenericResponse.<BlogResponse>builder()
                .data(blog)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable("id") String blogId) {
        blogService.deleteBlog(blogId);
    }

    @GetMapping("/interactive/{category}")
    public ResponseEntity<GenericResponse<List<BlogInteractiveResponse>>> findFiveBlogInteractive(@PathVariable("category") String categoryName) {
        List<BlogInteractiveResponse> blog = blogService.findFiveBlogInteractive(categoryName);
        GenericResponse<List<BlogInteractiveResponse>> response = GenericResponse.<List<BlogInteractiveResponse>>builder()
                .data(blog)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/emoji/{id}")
    public ResponseEntity<GenericResponse<Void>> updateBlogEmojis(@PathVariable("id") String blogId,
                                                                  @RequestBody UpdateEmojisRequest emojisRequest) {
        blogService.updateEmojiBlog(blogId, emojisRequest);
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/blog-id/{blogId}")
    public ResponseEntity<GenericResponse<BlogResponse>> getBlogDetailById(@PathVariable String blogId) {

        BlogResponse blogs = blogService.findBlogByBlogId(blogId);
        GenericResponse<BlogResponse> response = GenericResponse.<BlogResponse>builder()
                .data(blogs)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @GetMapping("/get-by-category")
    public ResponseEntity<GenericResponse<PagingResponse<BlogResponse>>> findBlogByCategory(@RequestParam(required = false, defaultValue = "1") int page,
                                                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                                                            @RequestParam(required = false, defaultValue = "createdDate") String field,
                                                                                            @RequestParam(required = false, defaultValue = "desc") String direction,
                                                                                            @RequestParam(required = false) String category) {
        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<BlogResponse> blog = blogService.getBlogByCategory(category, pagingRequest);
        GenericResponse<PagingResponse<BlogResponse>> response = GenericResponse.<PagingResponse<BlogResponse>>builder()
                .data(blog)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/blog-id/{blogId}")
    public ResponseEntity<GenericResponse<BlogResponse>> updateBlogByBlogId(@PathVariable("blogId") String blogId,
                                                        @RequestBody(required = false) UpdateRequestBlog request) {
        BlogResponse blog = blogService.updateBlogById(request, blogId);
        GenericResponse<BlogResponse> response = GenericResponse.<BlogResponse>builder()
                .data(blog)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }
}
