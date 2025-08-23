package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCommentRequest {
    @NotBlank(message = "Must have content")
    String content;
    @NotNull
    String commentId;
    @NotNull
    String userId;
}
