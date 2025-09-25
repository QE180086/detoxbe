package com.parttime.job.Application.projectmanagementservice.bannermanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.bannermanagement.entity.Banner;
import com.parttime.job.Application.projectmanagementservice.bannermanagement.response.BannerResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerResponse toDTO(Banner banner);
    List<BannerResponse> toListDTO(List<Banner> banners);
}