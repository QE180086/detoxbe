package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {
    @NotBlank(message = "Content not null")
    String content;
    @NotBlank(message = "Must have User Id")
    String userId;
    String commentParentId;
}
