package com.parttime.job.Application.projectmanagementservice.shippingmanagement.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogInfoResponse {
    private String status;
    private String updated_date;
}
