package com.parttime.job.Application.projectmanagementservice.cart.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.voucher.entity.Voucher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    private double totalPrice;
    private double discountedPrice;

    private boolean appliedVoucher;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        // Tính tổng giá từ tất cả cartItem
        if (cartItems != null && !cartItems.isEmpty()) {
            this.totalPrice = cartItems.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
        } else {
            this.totalPrice = 0.0;
        }

        // Nếu không có voucher thì discounted = total
        if (voucher == null || !Boolean.TRUE.equals(voucher.isActive())) {
            this.discountedPrice = this.totalPrice;
        } else {
            double discount = 0.0;
            if (this.totalPrice >= voucher.getMinOrderValue()) {
                discount = voucher.isPercentage()
                        ? this.totalPrice * (voucher.getDiscountValue() / 100.0)
                        : voucher.getDiscountValue();
            }
            this.discountedPrice = Math.max(0.0, this.totalPrice - discount);
        }
    }
    @PostLoad
    public void afterLoad() {
        calculateTotals();
    }
}

