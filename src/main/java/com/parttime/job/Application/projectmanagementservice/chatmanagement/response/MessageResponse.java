package com.parttime.job.Application.projectmanagementservice.chatmanagement.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private String id;
    private String roomId;
    private String senderId;
    private String senderName;
    private String senderAvatar;
    private String text;
    private String imageUrl;
    private LocalDateTime createdDate;
    private boolean seen;
    private LocalDateTime seenAt;
}
