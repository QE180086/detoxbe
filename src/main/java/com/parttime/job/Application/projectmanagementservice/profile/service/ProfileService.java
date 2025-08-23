package com.parttime.job.Application.projectmanagementservice.profile.service;

import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.profile.request.CreateProfileDefaultRequest;
import com.parttime.job.Application.projectmanagementservice.profile.request.UpdateProfileRequest;
import com.parttime.job.Application.projectmanagementservice.profile.response.ProfileResponse;

public interface ProfileService {
    /**
     * Create default profile when register user
     *
     * @param request
     */
    void createDefaultProfile(CreateProfileDefaultRequest request);

    /**
     * Update profile API
     *
     * @param request
     * @return
     */
    ProfileResponse updateProfile(UpdateProfileRequest request);

    /**
     * Get profile detail by userId
     *
     * @param userId
     * @return
     */
    ProfileResponse getDetailProfileByUserId(String userId);

    /**
     * Get profile detail by profileId
     *
     * @param profileId
     * @return
     */
    ProfileResponse getDetailProfileByProfileId(String profileId);

    /**
     * Get list profile and search, paging, sort
     *
     * @param searchText
     * @param pagingRequest
     * @return
     */
    PagingResponse<ProfileResponse> getListProfileAndSearch(String searchText, PagingRequest pagingRequest);

    /**
     * Set Default Address
     *
     * @param addressId
     * @return ProfileResponse
     */
    ProfileResponse setDefaultAddress(String addressId);

}
