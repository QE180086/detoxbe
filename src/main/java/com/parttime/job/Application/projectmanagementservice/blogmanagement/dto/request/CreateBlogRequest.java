package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBlogRequest {

    @NotBlank(message = "Title not null")
    String title;
    @NotBlank(message = "Content not null")
    String content;
    String image;
    boolean view = true;
    @NotBlank(message = "Must have user")
    String userId;
    @NotBlank(message = "Must have category")
    String categoryId;

}
