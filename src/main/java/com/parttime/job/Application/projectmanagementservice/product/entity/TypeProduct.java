package com.parttime.job.Application.projectmanagementservice.product.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeProduct extends BaseEntity {
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    String image;

    boolean isDeleted;
}
