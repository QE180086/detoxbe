package com.parttime.job.Application.projectmanagementservice.usermanagement.repository;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);

    @Query("SELECT u FROM User u " +
            "WHERE (:searchText IS NULL OR u.email LIKE %:searchText%) ")
    Page<User> getAllUserBySearchText(@Param("searchText") String searchText, PageRequest pageRequest);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    // Dashboard

    @Query("SELECT COUNT(u) FROM User u")
    Long countTotalUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.createdDate) = CURRENT_DATE")
    Long countTodayNewUsers();
}
