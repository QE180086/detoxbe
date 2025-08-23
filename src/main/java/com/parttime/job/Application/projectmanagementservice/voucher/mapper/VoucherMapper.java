package com.parttime.job.Application.projectmanagementservice.voucher.mapper;

import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    VoucherResponse toDTO(Voucher voucher);
    List<VoucherResponse> toListDTO(List<Voucher> vouchers);
}
