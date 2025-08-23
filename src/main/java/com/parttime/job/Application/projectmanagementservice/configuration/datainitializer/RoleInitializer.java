package com.parttime.job.Application.projectmanagementservice.configuration.datainitializer;

import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.StartDefinedRole;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.Role;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleInitializer {
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner initializeRoles() {
        return args -> {
            log.info("Initializing roles.....");
            initializeRole(StartDefinedRole.ADMIN);
            initializeRole(StartDefinedRole.USER);
            log.info("Role initialization completed .....");
        };
    }

    private void initializeRole(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(Role.builder()
                    .name(roleName)
                    .build());
            log.info("Role {} has been created", roleName);
        }
    }
}