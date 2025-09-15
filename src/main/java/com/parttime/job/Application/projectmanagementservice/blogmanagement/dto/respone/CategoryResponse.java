package com.parttime.job.Application.projectmanagementservice.blogmanagement.dto.respone;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    String id;
    Date createdDate;
    String name;
    Boolean isActive;
}
