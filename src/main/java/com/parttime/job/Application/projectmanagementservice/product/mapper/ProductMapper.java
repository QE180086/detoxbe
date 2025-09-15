package com.parttime.job.Application.projectmanagementservice.product.mapper;

import com.parttime.job.Application.projectmanagementservice.paymentmanagement.enumration.OrderStatus;
import com.parttime.job.Application.projectmanagementservice.paymentmanagement.repository.OrderItemRepository;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.product.entity.Rate;
import com.parttime.job.Application.projectmanagementservice.product.response.ProductResponse;
import com.parttime.job.Application.projectmanagementservice.product.response.StatisticsRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RateMapper.class})
public abstract class ProductMapper {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Mapping(target = "rateResponses", source = "rates")
    @Mapping(target = "statisticsRate", expression = "java(mapStatistics(product))")
    public abstract ProductResponse toDTO(Product product);

    public abstract List<ProductResponse> toListDTO(List<Product> product);

    protected StatisticsRate mapStatistics(Product product) {
        StatisticsRate stats = new StatisticsRate();
        if (product.getRates() != null && !product.getRates().isEmpty()) {
            stats.setTotalRate(product.getRates().size());
            double avg = product.getRates().stream()
                    .mapToInt(Rate::getRating)
                    .average()
                    .orElse(0.0);
            stats.setAverageRate(avg);
        }

        Integer totalSale = orderItemRepository.getTotalSoldByProductId(product.getId());
        stats.setTotalSale(totalSale != null ? totalSale : 0);

        return stats;
    }
}
