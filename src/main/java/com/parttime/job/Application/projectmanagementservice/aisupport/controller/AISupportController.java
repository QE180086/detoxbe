package com.parttime.job.Application.projectmanagementservice.aisupport.controller;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.projectmanagementservice.aisupport.request.AnswerRequest;
import com.parttime.job.Application.projectmanagementservice.aisupport.response.GeminiResponse;
import com.parttime.job.Application.projectmanagementservice.aisupport.service.AISupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class AISupportController {
    private final AISupportService aiSupportService;

    @PostMapping("/ask")
    public GenericResponse<String> askQuestion(@Valid @RequestBody AnswerRequest request) {
        String answer = aiSupportService.answerFromGemini(request);
        return GenericResponse.<String>builder()
                .data(answer)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M001_SUCCESS)
                        .messageDetail(MessageConstant.CREATE_DATA_SUCCESS)
                        .build())
                .isSuccess(true)
                .build();
    }
}
