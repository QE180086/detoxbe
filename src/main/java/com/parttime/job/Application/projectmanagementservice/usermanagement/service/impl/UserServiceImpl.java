package com.parttime.job.Application.projectmanagementservice.usermanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.common.utils.PagingUtil;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.OTPConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.StartDefinedRole;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.UserConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.Role;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.mapper.UserMapper;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ChangePasswordRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ForgetPasswordRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ResetPassword;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UpdateRoleRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.EmailService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.parttime.job.Application.common.constant.GlobalVariable.PAGE_SIZE_INDEX;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUtilService userUtilService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    @Override
    public PagingResponse<UserResponse> getAllUser(String searchText, PagingRequest pagingRequest) {

        Sort sort = PagingUtil.createSort(pagingRequest);
        PageRequest pageRequest = PageRequest.of(
                pagingRequest.getPage() - PAGE_SIZE_INDEX,
                pagingRequest.getSize(),
                sort
        );
        Page<User> userPage = userRepository.getAllUserBySearchText(searchText, pageRequest);
        if (userPage == null) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        List<UserResponse> userResponses = userMapper.toListUserDTO(userPage.getContent());
        return new PagingResponse<>(userResponses, pagingRequest, userPage.getTotalElements());
    }

    @Override
    public UserResponse changePassword(ChangePasswordRequest request) {
        User user = userUtilService.getCurrentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "New password is not matches with current password.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return userMapper.toUserDTO(user);
    }


    @Override
    public void forgetPassword(ForgetPasswordRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Email is not registered.");
        }
        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Username is not found.");
        }
        try {
            emailService.sendOTP(request.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponse resetPassword(ResetPassword resetPassword) {
        Optional<User> user = userRepository.findByEmail(resetPassword.getEmail());
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Email is not registered.");
        }
        if (!emailService.verifyOtp(resetPassword.getEmail(), resetPassword.getOtp())) {
            throw new AppException(MessageCodeConstant.M005_INVALID, OTPConstant.OTP_VALID_FAIL);
        }
        user.get().setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
        userRepository.save(user.get());
        return userMapper.toUserDTO(user.get());
    }

    @Override
    public UserResponse getDetailUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        return userMapper.toUserDTO(user.get());
    }

    @Override
    public UserResponse assignRole(String userId, UpdateRoleRequest request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        Optional<Role> role = roleRepository.findByName(request.getRoleName());
        if (role.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "Role is not found with role name " + request.getRoleName());
        }
        if (!Objects.equals(user.get().getRole().getName(), request.getRoleName())) {
            user.get().setRole(role.get());
            userRepository.save(user.get());
            return userMapper.toUserDTO(user.get());
        }
        throw new AppException(MessageCodeConstant.M026_FAIL, "Role is not saved with name  " + request.getRoleName());
    }

    @Override
    public UserResponse setStatus(String userId, UserStatus status) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        if (user.get().getStatus() != status) {
            user.get().setStatus(status);
            userRepository.save(user.get());
            return userMapper.toUserDTO(user.get());
        }
        return null;
    }

    @Override
    public UserResponse setDelete(String userId, boolean isDelete) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        if (user.get().isDeleted()!=isDelete) {
            user.get().setDeleted(isDelete);
            userRepository.save(user.get());
            return userMapper.toUserDTO(user.get());
        }
        throw new AppException(MessageCodeConstant.M002_ERROR, UserConstant.DELETE_USER_FAIL);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, UserConstant.USER_NOT_FOUND);
        }
        userRepository.delete(user.get());
    }
}
