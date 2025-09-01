package com.parttime.job.Application.projectmanagementservice.voucher.mapper;

import com.parttime.job.Application.projectmanagementservice.usermanagement.mapper.UserMapper;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.UserVoucher;
import com.parttime.job.Application.projectmanagementservice.voucher.response.UserVoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface UserVoucherMapper {
    UserVoucherResponse toDTO(UserVoucher userVoucher);
    List<UserVoucherResponse> toDTOList(List<UserVoucher> userVouchers);
}
