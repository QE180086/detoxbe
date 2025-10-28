package com.parttime.job.Application.projectmanagementservice.chatmanagement.service.impl;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.entity.Message;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.repository.MessageRepository;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.repository.RoomRepository;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.MessageResponse;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.service.ChatService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatCacheService cacheService;

    @Override
    public Message sendMessage(String roomId, String senderId, String text, String imageUrl) {
        return null;
    }

    @Override
    public void markAsSeen(String messageId, String userId) {

    }

    @Override
    public PagingResponse<MessageResponse> getMessages(String roomId, PagingRequest pagingRequest) {
        return null;
    }
}
