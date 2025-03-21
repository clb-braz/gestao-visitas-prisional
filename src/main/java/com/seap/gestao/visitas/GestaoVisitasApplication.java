package com.seap.gestao.visitas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "API de Gestão de Visitas Prisionais",
        version = "1.0",
        description = "Sistema de Gestão de Visitas para Unidade Prisional"
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class GestaoVisitasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoVisitasApplication.class, args);
    }
}
