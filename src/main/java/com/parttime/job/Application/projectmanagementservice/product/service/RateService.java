package com.parttime.job.Application.projectmanagementservice.product.service;

import com.parttime.job.Application.projectmanagementservice.product.request.RateRequest;
import com.parttime.job.Application.projectmanagementservice.product.request.UpdateRateRequest;
import com.parttime.job.Application.projectmanagementservice.product.response.RateResponse;

public interface RateService {
    RateResponse createRate(RateRequest request);
    RateResponse updateRate(UpdateRateRequest request);
    void deleteRate(String rateId);
}
