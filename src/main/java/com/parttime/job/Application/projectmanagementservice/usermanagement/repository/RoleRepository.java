package com.parttime.job.Application.projectmanagementservice.usermanagement.repository;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String roleName);
}
