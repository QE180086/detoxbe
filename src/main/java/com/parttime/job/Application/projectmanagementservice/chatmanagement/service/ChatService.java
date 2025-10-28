package com.parttime.job.Application.projectmanagementservice.chatmanagement.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.entity.Message;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.MessageResponse;

public interface ChatService {
    Message sendMessage(String roomId, String senderId, String text, String imageUrl);

    void markAsSeen(String messageId, String userId);

    PagingResponse<MessageResponse> getMessages(String roomId, PagingRequest pagingRequest);
}
