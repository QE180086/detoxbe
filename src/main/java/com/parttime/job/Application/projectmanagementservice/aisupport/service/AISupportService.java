package com.parttime.job.Application.projectmanagementservice.aisupport.service;

import com.parttime.job.Application.projectmanagementservice.aisupport.request.AnswerRequest;

public interface AISupportService {
    String answerFromGemini(AnswerRequest request);
}
