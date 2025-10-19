package com.parttime.job.Application.projectmanagementservice.shippingmanagement.entity;

import com.parttime.job.Application.common.entity.BaseEntity;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Orders;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class ShippingAddress extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Orders orders;

    String note;

    @Column(nullable = false)
    String receiverName;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false)
    String addressLine;
}
