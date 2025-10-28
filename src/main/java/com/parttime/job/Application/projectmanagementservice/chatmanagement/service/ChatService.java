package com.parttime.job.Application.projectmanagementservice.chatmanagement.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.MessageResponse;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.RoomResponse;

public interface ChatService {
    MessageResponse sendMessage(String roomId, String senderId, String text, String imageUrl);

    void markAsSeen(String messageId, String userId);

    PagingResponse<MessageResponse> getMessages(String roomId, PagingRequest pagingRequest);

    RoomResponse getOrCreateRoom(String userId1, String userId2);
}
