package com.parttime.job.Application.projectmanagementservice.profile.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.profile.request.UpdateProfileRequest;
import com.parttime.job.Application.projectmanagementservice.profile.response.ProfileResponse;
import com.parttime.job.Application.projectmanagementservice.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/profile")
@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PutMapping
    public ResponseEntity<GenericResponse<ProfileResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        GenericResponse<ProfileResponse> response = GenericResponse.<ProfileResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(profileService.updateProfile(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/address-default/{addressId}")
    public ResponseEntity<GenericResponse<ProfileResponse>> updateSetDefaultAddress(@PathVariable String addressId) {
        GenericResponse<ProfileResponse> response = GenericResponse.<ProfileResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(profileService.setDefaultAddress(addressId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<GenericResponse<ProfileResponse>> getDetailProfileByProfileId(@PathVariable String profileId) {
        GenericResponse<ProfileResponse> response = GenericResponse.<ProfileResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(profileService.getDetailProfileByProfileId(profileId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GenericResponse<ProfileResponse>> getDetailProfileByUserId(@PathVariable String userId) {
        GenericResponse<ProfileResponse> response = GenericResponse.<ProfileResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(profileService.getDetailProfileByUserId(userId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<ProfileResponse>>> getAllProfileBySearchText(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<ProfileResponse> profiles = profileService.getListProfileAndSearch(searchText, pagingRequest);

        GenericResponse<PagingResponse<ProfileResponse>> response = GenericResponse.<PagingResponse<ProfileResponse>>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(profiles)
                .build();
        return ResponseEntity.ok(response);
    }
}
