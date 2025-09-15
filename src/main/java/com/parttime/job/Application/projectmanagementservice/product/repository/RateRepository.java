package com.parttime.job.Application.projectmanagementservice.product.repository;

import com.parttime.job.Application.projectmanagementservice.product.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, String> {
}
