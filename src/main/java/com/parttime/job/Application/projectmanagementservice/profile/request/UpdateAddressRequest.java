package com.parttime.job.Application.projectmanagementservice.profile.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAddressRequest {
    private String street;
    private String city;
    private String state;
    private String country;
    private boolean isDefault;
    @Size(max = 50, message = "Other field must have less than 50 char")
    private String other;
}
