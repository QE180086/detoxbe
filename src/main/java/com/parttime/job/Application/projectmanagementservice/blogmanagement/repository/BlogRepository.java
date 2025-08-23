package com.parttime.job.Application.projectmanagementservice.blogmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
    @Query("SELECT b FROM Blog b " +
            "WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:content IS NULL OR b.content LIKE %:content%) " +
            "AND (:category IS NULL OR b.category.name LIKE %:category%)")
    Page<Blog> searchBlog(@Param("title") String title,
                          @Param("content") String content,
                          @Param("category") String category, PageRequest pageRequest);

    @Query("SELECT b FROM Blog b " +
            "JOIN b.category c " +
            "WHERE c.name LIKE %:categoryName% " +
            "ORDER BY b.createdDate DESC")
    List<Blog> findFiveBlogInteractive(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE (:title IS NULL OR b.title LIKE %:title%)")
    Page<Blog> getAllBlogBySearch(@Param("title") String searchText, PageRequest pageRequest);

    @Query("SELECT b FROM Blog b WHERE b.slugName = :slugName ")
    Optional<Blog> findBySlugName(@Param("slugName") String slugName);

    boolean existsBySlugName(String slugName);

    @Query("SELECT b FROM Blog b JOIN b.category c WHERE (:categoryName IS NULL OR c.name LIKE %:categoryName%)")
    Page<Blog> getAllBlogByCategory(@Param("categoryName") String categoryName, PageRequest pageRequest);
}
