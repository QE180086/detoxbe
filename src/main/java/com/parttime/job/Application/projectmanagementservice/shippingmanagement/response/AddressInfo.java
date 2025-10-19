package com.parttime.job.Application.projectmanagementservice.shippingmanagement.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressInfo {
    private int provinceId;
    private int districtId;
    private String wardCode;
    private String wardName;
    private String districtName;
    private String provinceName;
}
