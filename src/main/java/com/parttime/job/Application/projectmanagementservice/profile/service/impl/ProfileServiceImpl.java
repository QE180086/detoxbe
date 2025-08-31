package com.parttime.job.Application.projectmanagementservice.profile.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.profile.constant.ProfileConstant;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Address;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Information;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.profile.enumration.Gender;
import com.parttime.job.Application.projectmanagementservice.profile.mapper.AddressMapper;
import com.parttime.job.Application.projectmanagementservice.profile.mapper.ProfileMapper;
import com.parttime.job.Application.projectmanagementservice.profile.repository.AddressRepository;
import com.parttime.job.Application.projectmanagementservice.profile.repository.InformationRepository;
import com.parttime.job.Application.projectmanagementservice.profile.repository.ProfileRepository;
import com.parttime.job.Application.projectmanagementservice.profile.request.CreateProfileDefaultRequest;
import com.parttime.job.Application.projectmanagementservice.profile.request.UpdateProfileRequest;
import com.parttime.job.Application.projectmanagementservice.profile.response.ProfileResponse;
import com.parttime.job.Application.projectmanagementservice.profile.service.ProfileService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.UserConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final InformationRepository informationRepository;
    private final UserRepository userRepository;
    private final UserUtilService userUtilService;
    private final AddressMapper addressMapper;
    private final ProfileMapper profileMapper;

    @Override
    public void createDefaultProfile(CreateProfileDefaultRequest request) {
        Profile profile = new Profile();
        profile.setGender(Gender.MALE);
        profile.setFullName(request.getFullName());
        profile.setUser(request.getUser());
        profile.setAvatar("https://dongvat.edu.vn/upload/2025/01/avatar-zenitsu-cute-10.webp");
        profileRepository.save(profile);
    }

    @Override
    public ProfileResponse updateProfile(UpdateProfileRequest request) {
        Optional<User> user = userRepository.findById(userUtilService.getIdCurrentUser());
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, UserConstant.USER_NOT_FOUND);
        }
        Optional<Profile> profile = profileRepository.findByUserId(user.get().getId());

        updateProfilePerson(profile.get(), request);
        updateAddressProfile(profile.get(), request);
        updateInformationProfile(profile.get(), request);
        return profileMapper.toProfileResponse(profile.get());
    }

    @Override
    public ProfileResponse getDetailProfileByUserId(String userId) {
        Optional<Profile> profile = profileRepository.findByUserId(userId);
        if (profile.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        return profileMapper.toProfileResponse(profile.get());
    }

    @Override
    public ProfileResponse getDetailProfileByProfileId(String profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (profile.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, MessageConstant.DATA_NOT_FOUND);
        }
        return profileMapper.toProfileResponse(profile.get());
    }

    @Override
    public PagingResponse<ProfileResponse> getListProfileAndSearch(String searchText, PagingRequest pagingRequest) {
        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<Profile> profilePage = profileRepository.getProfileBySearchText(searchText, pageRequest);
        if (profilePage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, ProfileConstant.PROFILE_NOT_FOUND);
        }
        List<ProfileResponse> profileResponses = profileMapper.toListProfileResponse(profilePage.getContent());
        return new PagingResponse<>(profileResponses, pagingRequest, profilePage.getTotalElements());
    }

    @Override
    public ProfileResponse setDefaultAddress(String addressId) {
        String id = userUtilService.getIdCurrentUser();
        if (id == null) {
            throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, MessageConstant.UNAUTHORIZED);
        }
        Optional<Address> address = addressRepository.findById(addressId);
        if (address.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Address is not found");
        }
        Optional<Profile> profile = profileRepository.findByUserId(id);
        List<Address> addresses = profile.get().getAddresses();
        for (Address addr : addresses) {
            if (addr.isDefault()) {
                addr.setDefault(false);
                addressRepository.save(addr);
            }
        }
        address.get().setDefault(true);
        addressRepository.save(address.get());
        profileRepository.save(profile.get());

        return profileMapper.toProfileResponse(profile.get());
    }

    private void updateProfilePerson(Profile profile, UpdateProfileRequest request) {
        if (StringUtils.hasText(request.getAvatar())) {
            profile.setAvatar(request.getAvatar());
        }
        if (StringUtils.hasText(request.getNickName())) {
            profile.setNickName(request.getNickName());
        }
        if (StringUtils.hasText(request.getFullName())) {
            profile.setFullName(request.getFullName());
        }
        if (request.getGender() != null) {
            profile.setGender(request.getGender());
        }
        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        profileRepository.save(profile);
    }

    private void updateAddressProfile(Profile profile, UpdateProfileRequest request) {
        List<Address> addresses = profile.getAddresses();

        List<Address> addressList = addressMapper.toListEntity(request.getAddresses());
        if (addresses != addressList) {
            profile.getAddresses().clear();
            for (Address address : addressList) {
                address.setProfile(profile);
               address.setCreatedBy(userUtilService.getIdCurrentUser());
                address.setUpdatedBy(userUtilService.getIdCurrentUser());
            }
            profile.getAddresses().addAll(addressList);
            checkIsDefaultAddress(addressList);
            addressRepository.saveAll(addressList);
        }
    }

    private void updateInformationProfile(Profile profile, UpdateProfileRequest request) {
        Information information = profile.getInformation();
        if(information==null){
            information = new Information();
        }
        information.setProfile(profile);
        profile.setInformation(information);
        information.setUpdatedBy(userUtilService.getIdCurrentUser());
        if (StringUtils.hasText(request.getFacebook())) {
            information.setFacebook(request.getFacebook());
        }
        if (StringUtils.hasText(request.getInstagram())) {
            information.setInstagram(request.getInstagram());
        }
        if (StringUtils.hasText(request.getZalo())) {
            information.setZalo(request.getZalo());
        }
        if (StringUtils.hasText(request.getTiktok())) {
            information.setTiktok(request.getTiktok());
        }
        if (StringUtils.hasText(request.getTwitter())) {
            information.setTwitter(request.getTwitter());
        }
        informationRepository.save(information);
    }

    private void checkIsDefaultAddress(List<Address> address) {
        int count = 0;
        for (Address address1 : address) {
            if (address1.isDefault()) {
                count += 1;
            }
        }
        if (count > 1) {
            throw new AppException(MessageCodeConstant.M026_FAIL, "Address must have 1 default address");
        }
    }


}
