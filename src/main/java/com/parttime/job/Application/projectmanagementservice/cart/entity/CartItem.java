package com.parttime.job.Application.projectmanagementservice.cart.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart-item")
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

    private double unitPrice;

    private double totalPrice;

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        if (quantity >= 0) {
            this.totalPrice = unitPrice * quantity;
        } else {
            this.totalPrice = 0.0;
        }
    }
}
