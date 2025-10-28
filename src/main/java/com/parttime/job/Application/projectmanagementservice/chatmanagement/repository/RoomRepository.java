package com.parttime.job.Application.projectmanagementservice.chatmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.chatmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
}
