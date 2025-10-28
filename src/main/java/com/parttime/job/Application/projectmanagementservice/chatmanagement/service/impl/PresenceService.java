package com.parttime.job.Application.projectmanagementservice.chatmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PresenceService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ONLINE_KEY = "user:status:";

    public void setOnline(String userId) {
        redisTemplate.opsForValue().set(ONLINE_KEY + userId, "online", Duration.ofMinutes(30));
    }


    public void setOffline(String userId) {
        redisTemplate.delete(ONLINE_KEY + userId);
    }


    public boolean isOnline(String userId) {
        return "online".equals(redisTemplate.opsForValue().get(ONLINE_KEY + userId));
    }
}
