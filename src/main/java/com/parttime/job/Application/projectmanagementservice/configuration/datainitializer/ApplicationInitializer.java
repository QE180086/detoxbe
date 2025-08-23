package com.parttime.job.Application.projectmanagementservice.configuration.datainitializer;

import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.StartDefinedRole;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.Role;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitializer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @NonFinal
    @Value("${admin.username}")
    String adminUsername;

    @NonFinal
    @Value("${admin.password}")
    String adminPassword;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(StartDefinedRole.ADMIN)
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(StartDefinedRole.ADMIN)
                        .build());

                User user = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder().encode(adminPassword))
                        .status(UserStatus.ACTIVE)
                        .role(adminRole)
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with username {} and password: {}, please change it", adminUsername ,adminPassword);
            }
            log.info("Application initialization completed .....");
        };
    }
}