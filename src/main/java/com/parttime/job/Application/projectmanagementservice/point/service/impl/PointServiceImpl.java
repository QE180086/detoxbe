package com.parttime.job.Application.projectmanagementservice.point.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.constant.MessageConstantBlog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.mapper.PointMapper;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.point.request.PointRequest;
import com.parttime.job.Application.projectmanagementservice.point.response.PointResponse;
import com.parttime.job.Application.projectmanagementservice.point.service.PointService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.UserConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private PointRepository pointRepository;
    private PointMapper pointMapper;
    private UserUtilService userUtilService;

    @Override
    public PointResponse getPointByUserId() {
        String userId = userUtilService.getIdCurrentUser();
        if (userId == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        Point point = pointRepository.findByUserId(userId);
        if (point == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point not found for user");
        }

        return pointMapper.toDTO(point);
    }

    @Override
    public PointResponse addPoint(PointRequest request) {
        String userId = userUtilService.getIdCurrentUser();
        if (userId == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        Point point = pointRepository.findByUserId(userId);
        if (point == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point not found for user");
        }
        point.setCurrentPoints(point.getCurrentPoints() + request.getAmount());
        pointRepository.save(point);
        return pointMapper.toDTO(point);
    }

    @Override
    public PointResponse minusPoint(PointRequest request) {
        String userId = userUtilService.getIdCurrentUser();
        if (userId == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        Point point = pointRepository.findByUserId(userId);
        if (point == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point not found for user");
        }
        if( point.getCurrentPoints() < request.getAmount()) {
            throw new AppException(MessageCodeConstant.M026_FAIL, "Insufficient points");
        }
        point.setCurrentPoints(point.getCurrentPoints() - request.getAmount());
        pointRepository.save(point);
        return pointMapper.toDTO(point);
    }

    @Override
    public PagingResponse<PointResponse> getListPoint(String searchText, PagingRequest request) {
        Sort sort = PagingUtil.createSort(request);
        PageRequest pageRequest = PageRequest.of(
                request.getPage() - PAGE_SIZE_INDEX,
                request.getSize(),
                sort
        );
        Page<Point> pointPage = pointRepository.getAllPointBySearch(searchText, pageRequest);
        if (pointPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Point is not found");
        }
        List<PointResponse> pointResponse = pointMapper.toListDTO(pointPage.getContent());
        return new PagingResponse<>(pointResponse, request, pointPage.getTotalElements());
    }
}
