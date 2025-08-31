package com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {
    @Query("SELECT o FROM Orders o " +
            "WHERE (:searchText IS NULL OR LOWER(o.user.username) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "   OR CAST(o.id AS string) LIKE CONCAT('%', :searchText, '%'))")
    Page<Orders> searchAllOrder(@Param("searchText") String searchText, PageRequest pageRequest);

    @Query("SELECT o FROM Orders o " +
            "WHERE (:userId IS NULL OR o.user.id = :userId)")
    Page<Orders> searchAllOrderByUserId(@Param("userId") String userId, PageRequest pageRequest);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Orders o " +
            "WHERE o.user.id = :userId AND o.orderStatus = 'PENDING'")
    boolean existsPendingOrderByUserId(@Param("userId") String userId);

//    @Query("SELECT o FROM Orders o " +
//            "WHERE o.user.id = :userId AND o.orderStatus = 'PENDING' " +
//            "ORDER BY o.createdAt DESC")
//    Optional<Orders> findLatestPendingOrderByUserId(@Param("userId") String userId);

    Optional<Orders> findFirstByUserIdAndOrderStatusOrderByCreatedDateDesc(String userId, OrderStatus orderStatus);


}
