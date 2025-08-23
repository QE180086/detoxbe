package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchBlogByField {
    String title;
    String content;
    String category;
}
