package com.parttime.job.Application.projectmanagementservice.product.repository;

import com.parttime.job.Application.projectmanagementservice.product.entity.TypeProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeProductRepository extends JpaRepository<TypeProduct, String> {
    boolean existsByIdAndIsDeletedFalse(String id);
    @Query("SELECT p FROM TypeProduct p " +
            "WHERE (:searchText IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    Page<TypeProduct> searchTypeProductsByName(@Param("searchText") String searchText, PageRequest pageRequest);
}
