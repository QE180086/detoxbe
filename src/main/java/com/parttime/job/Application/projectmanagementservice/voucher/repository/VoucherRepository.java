package com.parttime.job.Application.projectmanagementservice.voucher.repository;

import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    Voucher findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT v FROM Voucher v WHERE (:code IS NULL OR v.code LIKE %:code%)")
    Page<Voucher> getAllVoucherBySearch(@Param("code") String code, PageRequest pageRequest);

}
