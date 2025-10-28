package com.parttime.job.Application.projectmanagementservice.chatmanagement.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomResponse {
    private String id;
    private String name;
    private boolean groupChat;
    private LocalDateTime createdDate;
}
