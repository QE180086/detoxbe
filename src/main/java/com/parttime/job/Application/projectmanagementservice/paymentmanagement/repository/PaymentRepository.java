package com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentMethod;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT p FROM Payment p WHERE p.orders.id = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") String orderId);

    Payment findByContent(String content);

    @Query("SELECT p FROM Payment p " +
            "WHERE (:userId IS NULL OR p.orders.user.id = :userId)")
    Page<Payment> searchAllPayment(@Param("userId") String userId, PageRequest pageRequest);

    @Modifying
    @Transactional
    @Query("UPDATE Payment p SET p.status = :paymentStatus WHERE p.orders.id = :orderId")
    void updatePaymentStatusByOrderId(@Param("orderId") String orderId,
                                      @Param("paymentStatus") PaymentStatus paymentStatus);


    // DashBoard
    @Query("SELECT DATE(p.createdDate), SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE p.status = 'COMPLETED' " +
            "GROUP BY DATE(p.createdDate) " +
            "ORDER BY DATE(p.createdDate)")
    List<Object[]> getDailyRevenue();

    @Query("SELECT DATE(p.createdDate), SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE p.status = 'COMPLETED' AND p.createdDate >= :startDate " +
            "GROUP BY DATE(p.createdDate) " +
            "ORDER BY DATE(p.createdDate)")
    List<Object[]> getRevenueFromDate(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "WHERE p.status = 'COMPLETED' " +
            "AND (:method IS NULL OR p.method = :method)")
    Double getTotalRevenueByMethod(@Param("method") PaymentMethod method);

    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "WHERE p.status = 'COMPLETED' " +
            "AND DATE(p.createdDate) = CURRENT_DATE")
    Double getTodayRevenue();

    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "WHERE p.status = 'COMPLETED' " +
            "AND p.method = :method " +
            "AND DATE(p.createdDate) = :date")
    Double getRevenueByMethodAndDate(@Param("method") PaymentMethod method,
                                     @Param("date") LocalDate date);
}
