package com.example.morax.config.openapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server


@OpenAPIDefinition(
    info = io.swagger.v3.oas.annotations.info.Info(
        contact = Contact(
            name = "Nguyen Thanh Nam",
            email = "kidpooh2k+morax@gmail.com",
            url = "https://github.com/AccepptedProblems"
        ),
        description = "OpenApi documentation for Spring Security",
        title = "OpenApi specification - Nam Nguyen",
        version = "1.0",
        license = License(name = "Licence name", url = "https://some-url.com"),
        termsOfService = "Terms of service"
    ),
    servers = [Server(
        description = "Local ENV",
        url = "http://localhost:8080"
    )],
    security = [SecurityRequirement(name = "bearerAuth")]
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    `in` = SecuritySchemeIn.HEADER
)
class OpenApiConfig() {}