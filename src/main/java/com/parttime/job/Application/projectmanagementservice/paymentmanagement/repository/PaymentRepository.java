package com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
//    @Query("SELECT p FROM Payment p " +
//            "WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', 'DC24', '%')) " +
//            "AND p.status = 'PENDING' " +
//            "AND p.user.id = :userId " +
//            "ORDER BY p.createdAt DESC")
//    Optional<Payment> findLatestPendingPaymentWithDC24ByUserId(@Param("userId") String userId);

    @Query("SELECT p FROM Payment p WHERE p.orders.id = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") String orderId);

    Payment findByContent(String content);
}
