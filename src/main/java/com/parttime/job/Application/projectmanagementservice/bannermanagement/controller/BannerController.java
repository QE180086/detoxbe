package com.parttime.job.Application.projectmanagementservice.bannermanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.request.BannerRequest;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.response.BannerResponse;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping
    public GenericResponse<PagingResponse<BannerResponse>> getListBanner(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(pageNumber, pageSize,
                new SortRequest(sortField, sortOrder));

        PagingResponse<BannerResponse> banners = bannerService.getListBanner(searchText, pagingRequest);
        return GenericResponse.<PagingResponse<BannerResponse>>builder()
                .data(banners)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();

    }

    @PostMapping
    public GenericResponse<BannerResponse> createBanner(@Valid @RequestBody BannerRequest request) {
        BannerResponse bannerResponse = bannerService.createBanner(request);
        return GenericResponse.<BannerResponse>builder()
                .data(bannerResponse)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();

    }

    @DeleteMapping("/{bannerId}")
    public void deleteBanner(@PathVariable("bannerId") String bannerId) {
        bannerService.deleteBanner(bannerId);
    }
}