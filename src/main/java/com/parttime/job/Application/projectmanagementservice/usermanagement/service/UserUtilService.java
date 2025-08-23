package com.parttime.job.Application.projectmanagementservice.usermanagement.service;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserUtilService {
    private final UserRepository userRepository;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        if (username != null) {
            Optional<User> user = userRepository.findByUsername(username);
            return user.orElse(null);
        }
        return null;
    }

    public String getIdCurrentUser() {
        String username = getCurrentUsername();
        if (username != null) {
            Optional<User> user = userRepository.findByUsername(username);
            return user.get().getId();
        }
        return null;
    }
}
