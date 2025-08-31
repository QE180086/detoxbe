package com.parttime.job.Application.projectmanagementservice.paymentmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toDTO(Payment payment);
}
