//package com.parttime.job.Application.projectmanagementservice.usermanagement.service.impl;
//
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
//import com.parttime.job.Application.common.constant.MessageCodeConstant;
//import com.parttime.job.Application.common.exception.AppException;
//import com.parttime.job.Application.projectmanagementservice.usermanagement.response.AuthResponseLogin;
//import com.parttime.job.Application.projectmanagementservice.usermanagement.service.GoogleAuthService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class GoogleAuthServiceImpl implements GoogleAuthService {
//    private final GoogleIdTokenVerifier verifier;
//
//    @Override
//    public Map<String, Object> verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//        if (idToken != null) {
//            GoogleIdToken.Payload payload = idToken.getPayload();
//            return Map.of(
//                    "email", payload.getEmail(),
//                    "name", payload.get("name"),
//                    "picture", payload.get("picture")
//            );
//        } else {
//            throw new AppException(MessageCodeConstant.M005_INVALID, "Invalid ID token.");
//        }
//    }
////
////    @Override
////    public AuthResponseLogin loginWithGoogle(String idTokenString) {
////        Map<String, Object> userInfo = verifyToken(idTokenString);
////
////        String email = (String) userInfo.get("email");
////        String name = (String) userInfo.get("name");
////        return null;
////    }
//}
