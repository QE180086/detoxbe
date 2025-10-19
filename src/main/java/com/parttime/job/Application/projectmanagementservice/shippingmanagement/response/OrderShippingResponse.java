package com.parttime.job.Application.projectmanagementservice.shippingmanagement.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderShippingResponse {
    private int shop_id;
    private int client_id;
    private String from_name;
    private String from_phone;
    private String from_address;
    private String to_name;
    private String to_phone;
    private String to_address;
    private String weight;
    private String cod_amount;
    private String custom_service_fee;
    private String content;
    private String note;
    private String order_code;
    private String leadtime;
    private String order_date;
    private String status;
    private List<LogInfoResponse> log;

}
