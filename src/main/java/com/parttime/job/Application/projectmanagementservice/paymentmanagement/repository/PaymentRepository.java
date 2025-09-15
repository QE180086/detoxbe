package com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT p FROM Payment p WHERE p.orders.id = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") String orderId);

    Payment findByContent(String content);

    @Query("SELECT p FROM Payment p " +
            "WHERE (:userId IS NULL OR p.orders.user.id = :userId)")
    Page<Payment> searchAllPayment(@Param("userId") String userId, PageRequest pageRequest);

}
