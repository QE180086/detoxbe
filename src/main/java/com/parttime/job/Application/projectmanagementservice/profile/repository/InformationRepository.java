package com.parttime.job.Application.projectmanagementservice.profile.repository;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<Information, String> {
}
