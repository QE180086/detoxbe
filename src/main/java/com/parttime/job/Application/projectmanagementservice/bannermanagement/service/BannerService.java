package com.parttime.job.Application.projectmanagementservice.bannermanagement.service;


import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.request.BannerRequest;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.response.BannerResponse;

public interface BannerService {
    BannerResponse createBanner(BannerRequest bannerRequest);
    void deleteBanner(String bannerId);
    PagingResponse<BannerResponse> getListBanner(String searchText, PagingRequest pagingRequest);
}
