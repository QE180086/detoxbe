package com.parttime.job.Application.projectmanagementservice.product.repository;

import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM Product p " +
            "WHERE (:searchText IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    Page<Product> searchProductsByName(@Param("searchText") String searchText, PageRequest pageRequest);

//    Integer countByTypeTarget(TypeTarget typeTarget);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.name = :name AND p.id <> :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") String id);

    // Dashboard

    @Query("SELECT COUNT(p) FROM Product p")
    Long countTotalProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE DATE(p.createdDate) = CURRENT_DATE")
    Long countTodayProducts();
}
