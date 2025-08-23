package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogInteractiveResponse {
    String title;
    String content;
    String image;
    int emojis;
    boolean view;
    String slugName;
}
