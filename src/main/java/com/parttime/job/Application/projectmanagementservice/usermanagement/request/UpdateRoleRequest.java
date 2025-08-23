package com.parttime.job.Application.projectmanagementservice.usermanagement.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    @NotNull
    private String roleName;
}
