package com.parttime.job.Application.projectmanagementservice.blogmanagement.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.CommentRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.UpdateCommentRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.UpdateEmojiComment;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CommentResponse;

public interface CommentService {
    PagingResponse<CommentResponse> getAllCommentInBlog(String blogId, PagingRequest pagingRequest);

    CommentResponse createComment(String blogId, CommentRequest commentRequest);

    CommentResponse updateComment(String blogId, UpdateCommentRequest updateCommentRequest);

    void deleteComment(String blogId, String commentId, String userId);

    void updateEmojisComment(String blogId, String commentId, String userId, UpdateEmojiComment updateEmojiComment);
}
