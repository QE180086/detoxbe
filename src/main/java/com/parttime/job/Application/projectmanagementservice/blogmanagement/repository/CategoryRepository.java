package com.parttime.job.Application.projectmanagementservice.blogmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("SELECT  c FROM Category c WHERE (:name IS NULL OR c.name LIKE %:name%)")
    Page<Category> getAllCategoryAndSearchText(@Param("name") String searchText, PageRequest pageRequest);

    @Query("SELECT DISTINCT c FROM Category c JOIN c.blogs b WHERE (:title IS NULL OR b.title LIKE %:title%)")
    Page<Category> getCategoryBySearchText(@Param("title") String searchText, PageRequest pageRequest);

    @Query("Select c.name from Category c ")
    List<String> findNameExistCategory();

}
