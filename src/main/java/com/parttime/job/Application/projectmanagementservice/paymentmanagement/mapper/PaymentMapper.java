package com.parttime.job.Application.projectmanagementservice.paymentmanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.entity.Payment;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.response.PaymentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface PaymentMapper {
    PaymentResponse toDTO(Payment payment);
    List<PaymentResponse> toListDTO(List<Payment> payment);

}
