package com.parttime.job.Application.projectmanagementservice.profile.repository;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    @Query("SELECT a FROM Address a " +
            "WHERE a.isDefault = true " +
            "AND a.profile.user.id = :userId")
    Optional<Address> findDefaultAddressByUserId(@Param("userId") String userId);
}
