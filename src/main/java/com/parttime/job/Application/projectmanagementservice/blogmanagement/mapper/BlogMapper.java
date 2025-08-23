package com.parttime.job.Application.projectmanagementservice.blogmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request.CreateBlogRequest;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogInteractiveResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone.BlogResponse;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Blog;
import com.parttime.job.Application.projectmanagementservice.blogmanagement.entity.Category;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "slugName", source = "slugName")
    @Mapping(target = "fullname", source = "user.profile.fullName")
    BlogResponse toBlogResponse(Blog blog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "category", source = "category")
    Blog toBlog(CreateBlogRequest request, User user, Category category);

    List<BlogResponse> toListBlogResponse(List<Blog> blogs);
    
    @Mapping(target = "slugName", source = "slugName")
    List<BlogInteractiveResponse> toListBlogInteractiveResponse(List<Blog> blogs);

}
