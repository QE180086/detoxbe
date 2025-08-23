package com.parttime.job.Application.projectmanagementservice.product.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    String name;
    double price;
    double salePrice;
    String image;
    @Column(name = "description", length = 2000)
    String description;
    @ManyToOne
    @JoinColumn(name = "type_product_id")
    TypeProduct typeProduct;
    boolean isActive;
}
