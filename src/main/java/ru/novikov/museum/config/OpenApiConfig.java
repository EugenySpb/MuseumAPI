package ru.novikov.museum.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "API booking events in museums",
                description = "System for booking tickets for events in museums", version = "1.0.0",
                contact = @Contact(
                        name = "Eugene Novikov",
                        email = "eugene.saintpetersburg@gmail.com"
                )
        )
)
public class OpenApiConfig {

}
