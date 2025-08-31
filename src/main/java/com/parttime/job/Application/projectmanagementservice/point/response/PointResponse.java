package com.parttime.job.Application.projectmanagementservice.point.response;

import lombok.Data;


@Data
public class PointResponse {
    private String id;
    private String userId;
    private String email;
    private int currentPoints;
}
