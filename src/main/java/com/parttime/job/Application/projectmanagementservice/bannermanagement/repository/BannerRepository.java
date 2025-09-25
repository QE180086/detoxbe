package com.parttime.job.Application.projectmanagementservice.bannermanagement.repository;

import com.parttime.job.Application.projectmanagementservice.bannermanagement.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String> {
    @Query("SELECT b FROM Banner b " +
            "WHERE lower(b.title) LIKE lower(concat('%', COALESCE(:search, ''), '%')) " +
            "AND lower(b.content) LIKE lower(concat('%', COALESCE(:search, ''), '%'))")
    Page<Banner> searchBanner(@Param("search") String search, PageRequest pageRequest);

}