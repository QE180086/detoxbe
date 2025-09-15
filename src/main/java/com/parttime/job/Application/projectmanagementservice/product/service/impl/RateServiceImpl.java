package com.parttime.job.Application.projectmanagementservice.product.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.product.entity.Rate;
import com.parttime.job.Application.projectmanagementservice.product.mapper.RateMapper;
import com.parttime.job.Application.projectmanagementservice.product.repository.ProductRepository;
import com.parttime.job.Application.projectmanagementservice.product.repository.RateRepository;
import com.parttime.job.Application.projectmanagementservice.product.request.RateRequest;
import com.parttime.job.Application.projectmanagementservice.product.request.UpdateRateRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.RateResponse;
import com.parttime.job.Application.projectmanagementservice.product.service.RateService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final RateRepository rateRepository;
    private final ProductRepository productRepository;
    private final UserUtilService userUtilService;
    private final RateMapper rateMapper;

    @Override
    public RateResponse createRate(RateRequest request) {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User not found");
        }
        Optional<Product> product = productRepository.findById(request.getProductId());
        if (product.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Product not found");
        }

        Rate rate = new Rate();
        rate.setComment(request.getComment());
        rate.setUser(user);
        rate.setProduct(product.get());
        rate.setRating(request.getRateValue());

        rateRepository.save(rate);
        return rateMapper.toDTO(rate);
    }

    @Override
    public RateResponse updateRate(UpdateRateRequest request) {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User not found");
        }
        Optional<Rate> rate = rateRepository.findById(request.getRateId());
        if (rate.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Rate not found");
        }
        if (request.getComment() != rate.get().getComment() && request.getComment() != null) {
            rate.get().setComment(request.getComment());
        }
        if (request.getRateValue() != rate.get().getRating() && request.getRateValue() != 0) {
            rate.get().setRating(request.getRateValue());
        }
        rateRepository.save(rate.get());
        return rateMapper.toDTO(rate.get());
    }

    @Override
    public void deleteRate(String rateId) {
        User user = userUtilService.getCurrentUser();
        if (user == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "User not found");
        }
        Optional<Rate> rate = rateRepository.findById(rateId);
        if (rate.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Rate not found");
        }
        if (user.getId() == rate.get().getUser().getId()) {
            rateRepository.delete(rate.get());
        } else {
            throw new AppException(MessageCodeConstant.M026_FAIL, "You are not the owner of this rate");
        }
    }
}
