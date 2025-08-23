package com.parttime.job.Application.projectmanagementservice.point.repository;

import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, String> {
    Point findByUserId(String userId);
    @Query("SELECT p FROM Point p WHERE (:email IS NULL OR LOWER(p.user.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<Point> getAllPointBySearch(@Param("email") String email, PageRequest request);
}
