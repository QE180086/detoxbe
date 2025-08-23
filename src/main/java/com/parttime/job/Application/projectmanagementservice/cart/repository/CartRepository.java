package com.parttime.job.Application.projectmanagementservice.cart.repository;

import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByUserId(String userId);
    Optional<Cart> findByUserIdAndIsActiveTrue(String userId);

    boolean existsByUserId(String userId);
    boolean existsByUserIdAndIsActiveTrue(String userId);

    @Query("SELECT c FROM Cart c WHERE (:username IS NULL OR LOWER(c.user.username) LIKE LOWER(CONCAT('%', :username, '%')))")
    Page<Cart> getAllCartBySearch(@Param("username") String username, PageRequest pageRequest);

}
