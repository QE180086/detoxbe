package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEmojiComment {
    @Min(value = 1, message = "Number emoji must have than 0")
    int emojis;
}
