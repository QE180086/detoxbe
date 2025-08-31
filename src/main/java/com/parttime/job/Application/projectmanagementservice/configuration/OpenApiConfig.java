package com.parttime.job.Application.projectmanagementservice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_AUTH = "bearerAuth";
    private static final String BEARER = "bearer";
    private static final String JWT = "JWT";

    private static final String DEV_SERVER_URLS = "http://localhost:8080;https://dextox-f9ajbedgghgvf7an.malaysiawest-01.azurewebsites.net/";

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = BEARER_AUTH;
        String[] urls = DEV_SERVER_URLS.split(";");
        List<Server> servers = new ArrayList<>();
        for (String item : urls) {
            servers.add(new Server().url(item));
        }

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(AUTHORIZATION)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(BEARER)
                                .bearerFormat(JWT)))
                .servers(servers);
    }
}
