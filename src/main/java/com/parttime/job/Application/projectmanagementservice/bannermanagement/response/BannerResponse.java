package com.parttime.job.Application.projectmanagementservice.bannermanagement.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BannerResponse {
    String id;
    String title;
    String content;
    String image;
    String url;
    boolean isActive;
    Date createdAt;
    String createdUser;
}
