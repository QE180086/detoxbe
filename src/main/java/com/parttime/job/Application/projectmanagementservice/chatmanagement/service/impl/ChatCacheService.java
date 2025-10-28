package com.parttime.job.Application.projectmanagementservice.chatmanagement.service.impl;

import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ChatCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LAST_MSG_KEY = "room:last:";

    public void saveLastMessage(String roomId, MessageResponse message) {
        redisTemplate.opsForValue().set(LAST_MSG_KEY + roomId, message, Duration.ofHours(6));
    }


    public MessageResponse getLastMessage(String roomId) {
        return (MessageResponse) redisTemplate.opsForValue().get(LAST_MSG_KEY + roomId);
    }
}
