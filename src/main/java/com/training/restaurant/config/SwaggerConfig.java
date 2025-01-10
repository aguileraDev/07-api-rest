package com.training.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Restaurant API")
                                .description("Documentación de la API de restaurante")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Equipo de desarrollo")
                                                .email("equipo@desarrollo.com")
                                )

                );
    }
}
