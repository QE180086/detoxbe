package com.parttime.job.Application.projectmanagementservice.blogmanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.CommentRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.UpdateCommentRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.UpdateEmojiComment;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CommentResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.service.CommentService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{blogId}")
    public ResponseEntity<GenericResponse<PagingResponse<CommentResponse>>> getAllCommentByBlogId(@PathVariable("blogId") String blogId,
                                                                                                  @RequestParam(required = false, defaultValue = "1") int page,
                                                                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                                                                  @RequestParam(required = false, defaultValue = "createdDate") String field,
                                                                                                  @RequestParam(required = false, defaultValue = "desc") String direction) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));
        PagingResponse<CommentResponse> commentResponses = commentService.getAllCommentInBlog(blogId, pagingRequest);
        GenericResponse<PagingResponse<CommentResponse>> response = GenericResponse.<PagingResponse<CommentResponse>>builder()
                .data(commentResponses)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{blogId}")
    public ResponseEntity<GenericResponse<CommentResponse>> createComment(@PathVariable("blogId") String blogId,
                                                                          @Valid @RequestBody CommentRequest request) {
        CommentResponse commentResponse = commentService.createComment(blogId, request);
        GenericResponse<CommentResponse> response = GenericResponse.<CommentResponse>builder()
                .data(commentResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.CREATE_DATA_SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<CommentResponse>> updateComment(@PathVariable("id") String id,
                                                                          @RequestBody UpdateCommentRequest request) {
        CommentResponse commentResponse = commentService.updateComment(id, request);
        GenericResponse<CommentResponse> response = GenericResponse.<CommentResponse>builder()
                .data(commentResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.CREATE_DATA_SUCCESS)
                        .build())
                .build();
        return ResponseEntity.ok(response);

    }

    @PutMapping("/emojis/{id}")
    public void updateEmojisComment(@PathVariable("id") String id,
                                    @RequestParam("commentId") String commentId,
                                    @RequestParam("userId") String userId,
                                    @Valid @RequestBody UpdateEmojiComment number) {

        commentService.updateEmojisComment(id, commentId, userId, number);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("id") String id,
                              @RequestParam("commentId") String commentId,
                              @RequestParam("userId") String userId) {
        commentService.deleteComment(id, commentId, userId);
    }

}
