package com.parttime.job.Application.projectmanagementservice.profile.mapper;

import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.profile.response.ProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    ProfileResponse toProfileResponse(Profile profile);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    List<ProfileResponse> toListProfileResponse(List<Profile> profiles);
}
