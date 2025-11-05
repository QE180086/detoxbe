package com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    @Query("SELECT SUM(ot.quantity) " +
            "FROM OrderItem ot " +
            "JOIN ot.orders o " +
            "WHERE ot.product.id = :productId " +
            "AND o.orderStatus = 'COMPLETED'")
    Integer getTotalSoldByProductId(@Param("productId") String productId);

    List<OrderItem> findByOrders_Id(String orderId);
}
