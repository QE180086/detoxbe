package com.parttime.job.Application.projectmanagementservice.chatmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.chatmanagement.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    Page<Message> findByRoomIdOrderByCreatedDateDesc(String roomId, PageRequest pageRequest);
    long countByRoomIdAndSeenFalseAndSenderIdNot(String roomId, String userId);
}
