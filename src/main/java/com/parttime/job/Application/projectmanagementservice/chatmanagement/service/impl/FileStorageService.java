package com.parttime.job.Application.projectmanagementservice.chatmanagement.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {
    private Cloudinary cloudinary;
    private static final String URL = "url";


    public String uploadSingle(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "File is empty");
        }

        try {
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "parttime_job_uploads"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return uploadResult.get(URL).toString();

        } catch (IOException e) {
            throw new AppException(MessageCodeConstant.M024_UPLOAD_FILE_FAIL, "Upload failed: " + e.getMessage());
        }
    }


    public List<String> uploadMultiple(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new AppException(MessageCodeConstant.M003_NOT_FOUND, "No files were uploaded");
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadSingle(file));
        }
        return urls;
    }
}
