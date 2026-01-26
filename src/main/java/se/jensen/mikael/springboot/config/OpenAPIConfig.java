package se.jensen.mikael.springboot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Konfigurerar Swagger / OpenAPI för vårt API.
 * Den här klassen säger till Swagger:
 * 1. Vad vårt API heter och vilken version det är.
 * 2. Att vi använder JWT (bearer-token) för säkerhet.
 * Tack vare detta kan vi se och testa alla endpoints i Swagger UI,
 * och skicka med token när vi testar endpoints som kräver inloggning.
 */
@OpenAPIDefinition(
        info = @Info(title = "API med JWT", version = "1.0"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPIConfig {

}

