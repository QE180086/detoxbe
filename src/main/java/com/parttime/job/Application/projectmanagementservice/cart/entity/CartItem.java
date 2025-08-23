package com.parttime.job.Application.projectmanagementservice.cart.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int quantity;

    private Double unitPrice;

    private Double totalPrice;

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        if (unitPrice != null && quantity >= 0) {
            this.totalPrice = unitPrice * quantity;
        } else {
            this.totalPrice = 0.0;
        }
    }
}
