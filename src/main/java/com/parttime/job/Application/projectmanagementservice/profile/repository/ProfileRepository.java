package com.parttime.job.Application.projectmanagementservice.profile.repository;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByUserId(String userId);
    @Query("SELECT p FROM Profile p " +
            "WHERE (:searchText IS NULL OR p.fullName LIKE %:searchText%) ")
    Page<Profile> getProfileBySearchText(@Param("searchText") String searchText, PageRequest pageRequest);

    @Query("SELECT p FROM Profile p JOIN p.user u WHERE u.username = :username")
    Profile getProfileByUserName(@Param("username") String userName);
}
