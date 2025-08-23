package com.parttime.job.Application.projectmanagementservice.blogmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.CategoryResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "isActive", source = "isActive")
    CategoryResponse toCategoryResponse(Category category);
    @Mapping(target = "isActive", source = "isActive")
    List<CategoryResponse> toListCategoryResponse(List<Category> categories);
}
