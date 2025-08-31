package com.parttime.job.Application.projectmanagementservice.paymentmanagement.service.impl;

import com.parttime.job.Application.common.exception.AppException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Service
public class VietQRService {
    private static final String BANK_ID = "970422";
    private static final String ACCOUNT_NO = "0379560889";

    public String generateFixedQRUrl(String content, double amount) {
        try {
            String encodedContent = URLEncoder.encode(content, "UTF-8");
            return String.format(
                    "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%d&addInfo=%s",
                    BANK_ID,
                    ACCOUNT_NO,
                    amount,
                    encodedContent
            );
        } catch (UnsupportedEncodingException e) {
            throw new AppException("Error encoding QR content", e.getMessage());
        }
    }
}
