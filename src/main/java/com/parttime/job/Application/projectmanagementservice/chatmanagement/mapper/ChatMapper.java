package com.parttime.job.Application.projectmanagementservice.chatmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.chatmanagement.entity.Message;
import com.parttime.job.Application.projectmanagementservice.chatmanagement.response.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", source = "sender.username")
    @Mapping(target = "senderAvatar", source = "sender.profile.avatar")
    MessageResponse toDTO(Message message);

    List<MessageResponse> toDTOList(List<Message> messages);
}
