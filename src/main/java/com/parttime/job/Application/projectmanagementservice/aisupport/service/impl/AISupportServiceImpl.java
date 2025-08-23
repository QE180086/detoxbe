package com.parttime.job.Application.projectmanagementservice.aisupport.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.aisupport.request.AnswerRequest;
import com.parttime.job.Application.projectmanagementservice.aisupport.request.GeminiRequest;
import com.parttime.job.Application.projectmanagementservice.aisupport.response.GeminiResponse;
import com.parttime.job.Application.projectmanagementservice.aisupport.service.AISupportService;
import com.parttime.job.Application.projectmanagementservice.product.entity.Product;
import com.parttime.job.Application.projectmanagementservice.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AISupportServiceImpl implements AISupportService {
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    private static final String DEFAULT_GEMINI_MODEL = "gemini-2.0-flash";
    private final String BASE_PRODUCT_URL = "http://localhost:3000/product/";

    public void refreshDetoxCache() {
        this.detoxProductsCache = productRepository.findAll();
    }
    private List<Product> detoxProductsCache = new ArrayList<>();

    @Value("${gemini.api.key}")
    private String apiKey;
    @Override
    public String answerFromGemini(AnswerRequest answerRequest) {
        String prompt;

        if (isDetoxRelatedQuestion(answerRequest.getQuestion())) {
            List<Product> detoxProducts = productRepository.findAll();

            if (detoxProducts.isEmpty()) {
                throw new AppException(MessageCodeConstant.M002_ERROR, "Không tìm thấy sản phẩm Detox nào.");
            }

            StringBuilder contentBuilder = new StringBuilder("Dưới đây là danh sách các sản phẩm Detox:\n");
            for (Product product : detoxProducts) {
                contentBuilder.append("- ")
                        .append(product.getName())
                        .append(": ")
                        .append(product.getDescription())
                        .append(" (Link: ")
                        .append(BASE_PRODUCT_URL).append(product.getId())
                        .append(")\n");
            }

           prompt = "Bạn là chuyên gia tư vấn sản phẩm.\n"
                    + "Dưới đây là các sản phẩm Detox:\n"
                    + contentBuilder
                    + "\nDựa trên danh sách trên, hãy trả lời câu hỏi sau bằng cách:\n"
                    + "- Chọn 1 sản phẩm phù hợp nhất.\n"
                    + "- Giải thích lý do lựa chọn.\n"
                    + "- Đưa link đến sản phẩm.\n\n"
                    + "Câu hỏi: " + answerRequest.getQuestion()
                    + "\n\nTrả lời:";
        } else {
            prompt = "Hãy trả lời câu hỏi. \n\n"
                    + "\n\nCâu hỏi: "
                    + answerRequest.getQuestion()
                    + "\n\nTrả lời:";
        }

        GeminiRequest.Part part = new GeminiRequest.Part(prompt);
        GeminiRequest.Content geminiContent = new GeminiRequest.Content(Collections.singletonList(part));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(geminiContent));
        String generateContentUrl = GEMINI_URL + DEFAULT_GEMINI_MODEL + ":generateContent?key=" + apiKey;

        try {
            GeminiResponse response = restTemplate.postForObject(generateContentUrl, request, GeminiResponse.class);

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                if (response.getCandidates().get(0).getContent() != null &&
                        response.getCandidates().get(0).getContent().getParts() != null &&
                        !response.getCandidates().get(0).getContent().getParts().isEmpty()) {
                    return response.getCandidates().get(0).getContent().getParts().get(0).getText();
                }
            }
            throw new AppException(MessageCodeConstant.M002_ERROR, "Không thể tạo phản hồi từ Gemini.");
        } catch (Exception e) {
            throw new AppException(MessageCodeConstant.M002_ERROR, "Lỗi khi gọi Gemini API: " + e.getMessage());
        }
    }


    private boolean isDetoxRelatedQuestion(String question) {
        String classifyPrompt = "Câu hỏi sau có liên quan đến sản phẩm Detox, thanh lọc cơ thể, giải độc không? "
                + "Hãy trả lời 'Có' hoặc 'Không'.\n\nCâu hỏi: " + question;

        GeminiRequest.Part part = new GeminiRequest.Part(classifyPrompt);
        GeminiRequest.Content content = new GeminiRequest.Content(Collections.singletonList(part));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(content));

        String classifyUrl = GEMINI_URL + DEFAULT_GEMINI_MODEL + ":generateContent?key=" + apiKey;

        try {
            GeminiResponse response = restTemplate.postForObject(classifyUrl, request, GeminiResponse.class);

            if (response != null && !response.getCandidates().isEmpty()) {
                String text = response.getCandidates().get(0).getContent().getParts().get(0).getText();
                return text.trim().toLowerCase().startsWith("có") || text.trim().toLowerCase().startsWith("yes");
            }
        } catch (Exception e) {
            System.err.println("Error checking topic relevance with Gemini: " + e.getMessage());
        }

        return false;
    }

}
