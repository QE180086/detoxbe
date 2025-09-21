package com.parttime.job.Application.projectmanagementservice.configuration.oauthgoogle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("da vao  fail ");
        response.sendRedirect("https://www.detoxcare.site/api/auth/error?message=" + exception.getMessage());
//        response.sendRedirect("http://localhost:8080/api/auth/error?message=" + exception.getMessage());

    }
}
