package com.parttime.job.Application.projectmanagementservice.chatmanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (IOException ignored) {
        }
    }

    public String store(MultipartFile file) {
        String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), root.resolve(filename));
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new AppException(MessageCodeConstant.M002_ERROR, e.getMessage());
        }
    }
}
