package com.parttime.job.Application.projectmanagementservice.bannermanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.entity.Banner;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.mapper.BannerMapper;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.repository.BannerRepository;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.request.BannerRequest;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.response.BannerResponse;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.service.BannerService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final UserRepository userRepository;
    private final UserUtilService userUtilService;

    private static final int PAGE_SIZE_INDEX = 1;

    @Override
    public BannerResponse createBanner(BannerRequest bannerRequest) {
        Optional<User> user = userRepository.findById(userUtilService.getIdCurrentUser());
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        String userCreated = user.get().getUsername();
        Banner banner = new Banner();
        banner.setUrl(bannerRequest.getUrl());
        banner.setTitle(bannerRequest.getTitle());
        banner.setContent(bannerRequest.getContent());
        banner.setCreatedBy(userUtilService.getIdCurrentUser());
        banner.setImage(bannerRequest.getImage());
        banner.setCreatedUser(userCreated);
        bannerRepository.save(banner);
        return bannerMapper.toDTO(banner);
    }

    @Override
    public void deleteBanner(String bannerId) {
        Optional<Banner> banner = bannerRepository.findById(bannerId);
        if (banner.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        bannerRepository.deleteById(bannerId);
    }

    @Override
    public PagingResponse<BannerResponse> getListBanner(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Banner> banners = bannerRepository.searchBanner(searchText, pageRequest);
        if (banners == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        List<BannerResponse> bannerResponses = bannerMapper.toListDTO(banners.getContent());
        return new PagingResponse<>(bannerResponses, pagingRequest, banners.getTotalElements());
    }
}
