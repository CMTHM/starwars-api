package com.example.starwars.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
//@OpenAPIDefinition
//@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {

		return new OpenAPI().info(new Info().title("Your API Title").version("1.0"))
				.addSecurityItem(new SecurityRequirement().addList("Authorization")) // Reference to the security scheme
				.components(new Components().addSecuritySchemes("Authorization", new SecurityScheme().name("Authorization")
						.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}
}
