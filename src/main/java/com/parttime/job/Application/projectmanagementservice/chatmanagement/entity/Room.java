package com.parttime.job.Application.projectmanagementservice.chatmanagement.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room extends BaseEntity {
    private String name;
    private boolean groupChat;
}
