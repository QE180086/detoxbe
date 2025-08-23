package com.parttime.job.Application.projectmanagementservice.profile.mapper;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Address;
import com.parttime.job.Application.projectmanagementservice.profile.request.UpdateAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(UpdateAddressRequest request);

    List<Address> toListEntity(List<UpdateAddressRequest> request);
}
