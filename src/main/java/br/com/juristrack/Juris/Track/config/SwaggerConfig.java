package br.com.juristrack.Juris.Track.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("Juristrack API")
                        .version("1.0")
                        .description("""
                            A robust REST API for legal management systems, enabling the registration of attorneys and clients, document handling, file uploads, 
                            and structured data organization. Developed with best practices to ensure scalability, maintainability, and security.
                        """)
        );
    }
}
