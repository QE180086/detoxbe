package com.parttime.job.Application.projectmanagementservice.product.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class TypeProductResponse {
    String id;
    String name;
    String description;
    String image;
    boolean isDeleted;
}
