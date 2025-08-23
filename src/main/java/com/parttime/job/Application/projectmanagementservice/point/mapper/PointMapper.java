package com.parttime.job.Application.projectmanagementservice.point.mapper;

import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.response.PointResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointMapper {
    @Mapping(target ="userId" , source = "point.user.id")
    @Mapping(target = "email", source = "point.user.email")
    @Mapping(target = "voucherResponses", source = "point.vouchers")
    PointResponse toDTO(Point point);
    List<PointResponse> toListDTO(List<Point> points);
}
