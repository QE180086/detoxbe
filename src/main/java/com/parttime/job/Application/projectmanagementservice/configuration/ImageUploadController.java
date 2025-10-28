package com.parttime.job.Application.projectmanagementservice.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {
    private final Cloudinary cloudinary;

    private static final String URL = "url";

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public GenericResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return GenericResponse.<String>builder()
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M003_NOT_FOUND)
                            .messageDetail(MessageConstant.DATA_NOT_FOUND)
                            .build())
                    .build();
        }
        try {
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "timestamp", System.currentTimeMillis() / 1000L,
                    "resource_type", "auto"
            );
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return GenericResponse.<String>builder()
                    .data(uploadResult.get(URL).toString())
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M001_SUCCESS)
                            .messageDetail(MessageConstant.SUCCESS)
                            .build())
                    .build();
        } catch (IOException e) {
            return GenericResponse.<String>builder()
                    .data(MessageConstant.UPLOAD_FILE_FAIL + e.getMessage())
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M024_UPLOAD_FILE_FAIL)
                            .messageDetail(MessageConstant.UPLOAD_FILE_FAIL)
                            .build())
                    .build();
        }
    }

    @PostMapping(value = "/upload-multiple", consumes = "multipart/form-data")
    public GenericResponse<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return GenericResponse.<List<String>>builder()
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M003_NOT_FOUND)
                            .messageDetail(MessageConstant.DATA_NOT_FOUND)
                            .build())
                    .build();
        }

        List<String> urls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                Map<String, Object> uploadOptions = ObjectUtils.asMap(
                        "timestamp", System.currentTimeMillis() / 1000L,
                        "resource_type", "auto"
                );

                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
                urls.add(uploadResult.get("url").toString());
            }

            return GenericResponse.<List<String>>builder()
                    .data(urls)
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M001_SUCCESS)
                            .messageDetail(MessageConstant.SUCCESS)
                            .build())
                    .build();

        } catch (IOException e) {
            return GenericResponse.<List<String>>builder()
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M024_UPLOAD_FILE_FAIL)
                            .messageDetail(MessageConstant.UPLOAD_FILE_FAIL + ": " + e.getMessage())
                            .build())
                    .build();
        }
    }

}
