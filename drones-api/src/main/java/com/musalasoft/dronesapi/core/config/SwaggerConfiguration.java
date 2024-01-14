package com.musalasoft.dronesapi.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(name = "Timothy Ngonadi", email = "timothyolisaeloka@gmail.com", url = "lorem"),
                description = "Open API documentation for Drone Service.",
                title = "Musala Soft Drone Service",
                version = "1.0",
                license = @License(name = "Apache License", url = "https://www.apache.org/licenses/LICENSE-2"),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "DEV ENV",
                        url = "http://localhost:8080"
                )
        }
)
public class SwaggerConfiguration {
}
