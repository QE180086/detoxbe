package com.parttime.job.Application.projectmanagementservice.voucher.repository;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.UserVoucher;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, String> {
    boolean existsByUserAndVoucher(User user, Voucher voucher);

    @Query("SELECT uv FROM UserVoucher uv WHERE uv.user.id = :userId AND uv.voucher.id = :voucherId")
    Optional<UserVoucher> findByUserIdAndVoucherId(@Param("userId") String userId, @Param("voucherId") String voucherId);

    @Query("SELECT uv FROM UserVoucher uv WHERE uv.user.id = :userId")
    Page<UserVoucher> getAllVoucherByUserId(@Param("userId") String userId, PageRequest pageRequest);
}
