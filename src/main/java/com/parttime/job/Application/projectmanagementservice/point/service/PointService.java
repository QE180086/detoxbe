package com.parttime.job.Application.projectmanagementservice.point.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.point.request.PointRequest;
import com.parttime.job.Application.projectmanagementservice.point.response.PointResponse;

public interface PointService {
    PointResponse getPointByUserId();
    PointResponse addPoint(PointRequest request);
    PointResponse minusPoint(PointRequest request);
    PagingResponse<PointResponse> getListPoint(String searchText, PagingRequest request);
}
