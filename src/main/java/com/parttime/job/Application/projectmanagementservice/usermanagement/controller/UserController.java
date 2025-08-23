package com.parttime.job.Application.projectmanagementservice.usermanagement.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.request.PagingRequest;
import com.parttime.job.Application.common.request.SortRequest;
import com.parttime.job.Application.common.response.PagingResponse;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.StartDefinedRole;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.UserConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ChangePasswordRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ForgetPasswordRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.ResetPassword;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UpdateRoleRequest;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<GenericResponse<PagingResponse<UserResponse>>> getAllUserBySearchText(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String field,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchText) {


        PagingRequest pagingRequest = new PagingRequest(page, size,
                new SortRequest(direction, field));

        PagingResponse<UserResponse> users = userService.getAllUser(searchText, pagingRequest);

        GenericResponse<PagingResponse<UserResponse>> response = GenericResponse.<PagingResponse<UserResponse>>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<GenericResponse<UserResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        GenericResponse<UserResponse> response = GenericResponse.<UserResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(userService.changePassword(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<GenericResponse<Void>> forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        userService.forgetPassword(request);
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponse<UserResponse>> resetPassword(@Valid @RequestBody ResetPassword request) {
        GenericResponse<UserResponse> response = GenericResponse.<UserResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(userService.resetPassword(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-detail/{userId}")
    public ResponseEntity<GenericResponse<UserResponse>> getDetailUser(@PathVariable String userId) {
        GenericResponse<UserResponse> response = GenericResponse.<UserResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.SUCCESS)
                        .build())
                .isSuccess(true)
                .data(userService.getDetailUser(userId))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/assign-role/{userId}")
    public ResponseEntity<GenericResponse<UserResponse>> assignRoleForUser(@PathVariable String userId,@Valid @RequestBody UpdateRoleRequest role) {
        GenericResponse<UserResponse> response = GenericResponse.<UserResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(UserConstant.UPDATE_USER_SUCCESS)
                        .build())
                .isSuccess(true)
                .data(userService.assignRole(userId, role))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/status/{userId}")
    public ResponseEntity<GenericResponse<UserResponse>> setStatus(@PathVariable String userId, @RequestParam UserStatus status) {
        GenericResponse<UserResponse> response = GenericResponse.<UserResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(UserConstant.UPDATE_USER_SUCCESS)
                        .build())
                .isSuccess(true)
                .data(userService.setStatus(userId, status))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/delete/{userId}")
    public ResponseEntity<GenericResponse<UserResponse>> setDelete(@PathVariable String userId, @RequestParam(defaultValue = "true") boolean isDeleted) {
        GenericResponse<UserResponse> response = GenericResponse.<UserResponse>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(UserConstant.UPDATE_USER_SUCCESS)
                        .build())
                .isSuccess(true)
                .data(userService.setDelete(userId, isDeleted))
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<GenericResponse<Void>> deleteUser(@PathVariable("id") String userId) {
        userService.deleteUser(userId);
        GenericResponse<Void> response = GenericResponse.<Void>builder()
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(UserConstant.DELETE_USER_SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }

}
