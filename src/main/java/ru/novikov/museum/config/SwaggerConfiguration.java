package ru.novikov.museum.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("CSRF Token"))
                .components(new Components()
                        .addSecuritySchemes("CSRF Token", createCSRFTokenScheme()))
                .info(new Info().title("API service for online booking of tickets for museum events")
                        .description("""
                                Service for online booking of tickets for museum events. \
                                
                                The application is designed to process user requests for registration for \
                                a selected event, create new events, manage events, and also allows museum employees to make changes. \
                                
                                The application is designed for implementation in the work of non-profit organizations.""")
                        .version("1.0.0")
                        .contact(new Contact().name("Eugene Novikov")
                                .email("eugene.saintpetersburg@gmail.com")));
    }

    private SecurityScheme createCSRFTokenScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .name("X-XSRF-TOKEN")
                .in(SecurityScheme.In.HEADER);
    }
}
