package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRequestBlog {
    @NotBlank(message = "Title not blank")
    String title;
    @NotBlank(message = "Title not blank")
    String content;
    String image;
    String categoryId;
}
