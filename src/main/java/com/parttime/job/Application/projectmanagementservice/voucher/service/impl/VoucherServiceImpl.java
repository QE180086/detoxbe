package com.parttime.job.Application.projectmanagementservice.voucher.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import com.parttime.job.Application.projectmanagementservice.voucher.mapper.VoucherMapper;
import com.parttime.job.Application.projectmanagementservice.voucher.repository.VoucherRepository;
import com.parttime.job.Application.projectmanagementservice.voucher.request.VoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private VoucherRepository voucherRepository;
    private VoucherMapper voucherMapper;

    @Override
    public PagingResponse<VoucherResponse> getAllVouchers(String searchText, PagingRequest request) {
        Sort sort = PagingUtil.createSort(request);
        PageRequest pageRequest = PageRequest.of(
                request.getPage() - PAGE_SIZE_INDEX,
                request.getSize(),
                sort
        );
        Page<Voucher> voucherPage = voucherRepository.getAllVoucherBySearch(searchText, pageRequest);
        if (voucherPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Voucher is not found");
        }
        List<VoucherResponse> voucherResponse = voucherMapper.toListDTO(voucherPage.getContent());
        return new PagingResponse<>(voucherResponse, request, voucherPage.getTotalElements());
    }

    @Override
    public VoucherResponse create(VoucherRequest request) {
        if (voucherRepository.existsByCode(request.getCode())) {
            throw new AppException(MessageCodeConstant.M004_DUPLICATE, "Voucher code already exists");
        }

        Voucher voucher = new Voucher();
        voucher.setCode(request.getCode());
        voucher.setPercentage(request.isPercentage());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinOrderValue(request.getMinOrderValue());
        voucher.setActive(request.isActive());
        voucher.setImage(request.getImage());
        voucherRepository.save(voucher);
        return voucherMapper.toDTO(voucher);
    }

    @Override
    public VoucherResponse getVoucherById(String voucherId) {
        Optional<Voucher> voucher = voucherRepository.findById(voucherId);
        if (voucher.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Voucher not found");
        }
        return voucherMapper.toDTO(voucher.get());
    }

    @Override
    public VoucherResponse updateVoucher(String voucherId, VoucherRequest request) {
        Voucher existingVoucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Voucher not found with ID: " + voucherId));

        boolean updated = false;
        if (!Objects.equals(existingVoucher.getCode(), request.getCode())) {
            existingVoucher.setCode(request.getCode());
            updated = true;
        }
        if (existingVoucher.getDiscountValue() != request.getDiscountValue()) {
            existingVoucher.setDiscountValue(request.getDiscountValue());
            updated = true;
        }
        if (existingVoucher.isPercentage() != request.isPercentage()) {
            existingVoucher.setPercentage(request.isPercentage());
            updated = true;
        }
        if (existingVoucher.getMinOrderValue() != request.getMinOrderValue()) {
            existingVoucher.setMinOrderValue(request.getMinOrderValue());
            updated = true;
        }
        if (existingVoucher.isActive() != request.isActive()) {
            existingVoucher.setActive(request.isActive());
            updated = true;
        }
        if (!Objects.equals(existingVoucher.getImage(), request.getImage())) {
            existingVoucher.setImage(request.getImage());
            updated = true;
        }
        if (updated) {
            existingVoucher = voucherRepository.save(existingVoucher);
        }
        return voucherMapper.toDTO(existingVoucher);
    }


    @Override
    public void deleteVoucher(String voucherId) {
        Optional<Voucher> voucher = voucherRepository.findById(voucherId);
        if (voucher.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Voucher not found");
        }
        voucherRepository.delete(voucher.get());
    }

}
