package com.parttime.job.Application.projectmanagementservice.profile.repository;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformationRepository extends JpaRepository<Information, String> {
    @Query("SELECT i FROM Information i " +
            "WHERE i.profile.user.id = :userId")
    Optional<Information> findByUserId(@Param("userId") String userId);
}
