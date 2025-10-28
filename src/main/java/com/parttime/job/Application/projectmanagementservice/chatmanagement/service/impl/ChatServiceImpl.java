package com.parttime.job.Application.projectmanagementservice.chatmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.entity.Message;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.entity.Room;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.mapper.ChatMapper;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.repository.MessageRepository;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.repository.RoomRepository;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.MessageResponse;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.RoomResponse;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.service.ChatService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatCacheService cacheService;
    private final ChatMapper chatMapper;

    @Override
    public MessageResponse sendMessage(String roomId, String senderId, String text, String imageUrl) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Room not found"));
        User sender = userRepository.findById(senderId).orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Sender not found"));


        Message m = new Message();
        m.setRoom(room);
        m.setSender(sender);
        m.setText(text);
        m.setImageUrl(imageUrl);
        m.setSeen(false);

        messageRepository.save(m);

        MessageResponse dto = chatMapper.toDTO(m);
// push to topic
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, dto);

// cache last message in redis
        cacheService.saveLastMessage(roomId, dto);

        return dto;
    }

    @Override
    public void markAsSeen(String messageId, String userId) {
        Message m = messageRepository.findById(messageId).orElseThrow(()-> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Message not found"));
        if (!m.getSender().getId().equals(userId) && !m.isSeen()) {
            m.setSeen(true);
            m.setSeenAt(LocalDateTime.now());
            messageRepository.save(m);
            messagingTemplate.convertAndSend("/topic/rooms/" + m.getRoom().getId(),
                    new SeenPayload(messageId, m.getSeenAt(), userId));
        }
    }

    @Override
    public PagingResponse<MessageResponse> getMessages(String roomId, PagingRequest request) {
        Sort sort = PagingUtil.createSort(request);
        PageRequest pageRequest = PageRequest.of(
                request.getPage() - PAGE_SIZE_INDEX,
                request.getSize(),
                sort
        );
        Page<Message> messagePage = messageRepository.findByRoomIdOrderByCreatedDateDesc(roomId, pageRequest);
        if (messagePage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Message is not found");
        }
        List<MessageResponse> messageResponse = chatMapper.toDTOList(messagePage.getContent());
        return new PagingResponse<>(messageResponse, request, messagePage.getTotalElements());
    }

    @Override
    public RoomResponse getOrCreateRoom(String userId1, String userId2) {
        return null;
    }

    public record SeenPayload(String messageId, LocalDateTime seenAt, String seenBy) {}
}
