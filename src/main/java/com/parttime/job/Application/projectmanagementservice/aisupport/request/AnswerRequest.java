package com.parttime.job.Application.projectmanagementservice.aisupport.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerRequest {
    @NotBlank(message = "Question cannot be blank")
    private String question;

}
