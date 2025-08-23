package com.parttime.job.Application.projectmanagementservice.cart.repository;

import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    @Query("""
                SELECT ci FROM CartItem ci
                WHERE ci.cart.id = :cartId
                  AND ci.product.id = :productId
                  AND ci.cart.isActive = true
            """)
    Optional<CartItem> findByCartIdAndProductIdAndCartIsActiveTrue(
            @Param("cartId") String cartId,
            @Param("productId") String productId
    );

    void deleteAllByCart_Id(String cartId);

    @Query("""
                SELECT ci FROM CartItem ci
                WHERE ci.cart.id = :cartId
            """)
    Page<CartItem> getAllCartItem(@Param("cartId") String cartId, PageRequest pageRequest);

}
