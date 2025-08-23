package com.parttime.job.Application.projectmanagementservice.profile.repository;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
}
