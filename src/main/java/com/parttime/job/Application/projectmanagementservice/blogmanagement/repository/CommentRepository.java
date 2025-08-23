package com.parttime.job.Application.projectmanagementservice.blogmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("Select c from Comment c where c.blog.id = :blogId AND c.level =1")
    Page<Comment> getListCommentByBlogId(@Param("blogId") String blogId, Pageable pageable);
}
