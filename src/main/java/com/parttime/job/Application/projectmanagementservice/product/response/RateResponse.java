package com.parttime.job.Application.projectmanagementservice.product.response;

import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RateResponse {
    private String id;
    private int rating;
    private String comment;
    private String productId;
    private String productName;
    private String fullName;
    private String avatar;
    private LocalDateTime createdDate;
}
