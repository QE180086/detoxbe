package com.parttime.job.Application.projectmanagementservice.voucher.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.UserConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.UserVoucher;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import com.parttime.job.Application.projectmanagementservice.voucher.mapper.UserVoucherMapper;
import com.parttime.job.Application.projectmanagementservice.voucher.mapper.VoucherMapper;
import com.parttime.job.Application.projectmanagementservice.voucher.repository.UserVoucherRepository;
import com.parttime.job.Application.projectmanagementservice.voucher.repository.VoucherRepository;
import com.parttime.job.Application.projectmanagementservice.voucher.request.ExchangeVoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.request.UserVoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.request.VoucherRequest;
import com.parttime.job.Application.projectmanagementservice.voucher.response.UserVoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.response.VoucherResponse;
import com.parttime.job.Application.projectmanagementservice.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    private final UserVoucherRepository userVoucherRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final UserVoucherMapper userVoucherMapper;
    private final UserUtilService userUtilService;
    private final PointRepository pointRepository;

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
        voucher.setExchangePoint(request.getExchangePoint());
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
        if (!Objects.equals(existingVoucher.getExchangePoint(), request.getExchangePoint())) {
            existingVoucher.setExchangePoint(request.getExchangePoint());
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

    @Override
    public UserVoucherResponse userReceiveVoucher(UserVoucherRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND));

        Voucher voucher = voucherRepository.findById(request.getVoucherId()).orElseThrow(
                () -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Voucher not found"));

        if (userVoucherRepository.existsByUserAndVoucher(user, voucher)) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "User has already received this voucher");
        }

        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucher);
        userVoucher.setUsed(false);
        userVoucherRepository.save(userVoucher);

        return userVoucherMapper.toDTO(userVoucher);
    }

    @Override
    public PagingResponse<UserVoucherResponse> getVouchersByUser(String userId, PagingRequest request) {
        Sort sort = PagingUtil.createSort(request);
        PageRequest pageRequest = PageRequest.of(
                request.getPage() - PAGE_SIZE_INDEX,
                request.getSize(),
                sort
        );
        Page<UserVoucher> userVoucherPage = userVoucherRepository.getAllVoucherByUserId(userId, pageRequest);
        if (userVoucherPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User voucher is not found");
        }
        List<UserVoucherResponse> userVoucherResponse = userVoucherMapper.toDTOList(userVoucherPage.getContent());
        return new PagingResponse<>(userVoucherResponse, request, userVoucherPage.getTotalElements());
    }

    @Override
    public UserVoucherResponse exchangeVoucher(ExchangeVoucherRequest request) {
        Optional<User> user = userRepository.findById(userUtilService.getIdCurrentUser());
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }

        Voucher voucher = new Voucher();
        voucher.setCode(generateRandomCode(6));
        voucher.setPercentage(request.isPercentage());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinOrderValue(request.getMinOrderValue());
        voucher.setActive(request.isActive());
        voucher.setImage(request.getImage());
        voucher.setExchangePoint(request.getExchangePoint());

        if (voucherRepository.existsByCode(voucher.getCode())) {
            throw new AppException(MessageCodeConstant.M004_DUPLICATE, "Voucher code already exists");
        }
        voucherRepository.save(voucher);

        Point point = pointRepository.findByUserId(user.get().getId());
        if (point == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point not found for user");
        }
        if (point.getCurrentPoints() - voucher.getExchangePoint() < 0) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Not enough points to exchange voucher");
        }
        point.setCurrentPoints(point.getCurrentPoints() - voucher.getExchangePoint());
        pointRepository.save(point);

        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setUser(user.get());
        userVoucher.setVoucher(voucher);
        userVoucher.setUsed(false);
        userVoucherRepository.save(userVoucher);
        return userVoucherMapper.toDTO(userVoucher);

    }

    public static String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
