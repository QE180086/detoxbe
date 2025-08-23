package com.parttime.job.Application.projectmanagementservice.configuration;

import com.parttime.job.Application.projectmanagementservice.usermanagement.service.UserUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final UserUtilService userUtilService;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(userUtilService.getIdCurrentUser());
    }
}