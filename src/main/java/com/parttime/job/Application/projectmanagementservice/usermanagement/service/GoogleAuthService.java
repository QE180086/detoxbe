package com.parttime.job.Application.projectmanagementservice.usermanagement.service;

import com.parttime.job.Application.projectmanagementservice.usermanagement.response.AuthResponseLogin;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface GoogleAuthService {
    Map<String, Object> verifyToken(String idTokenString) throws GeneralSecurityException, IOException;
    AuthResponseLogin loginWithGoogle(String idTokenString);
}
