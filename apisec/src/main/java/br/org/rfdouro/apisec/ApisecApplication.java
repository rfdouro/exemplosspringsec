package br.org.rfdouro.apisec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
public class ApisecApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApisecApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList("Auth JWT"))
				.components(new Components()
						.addSecuritySchemes("Auth JWT",
								new SecurityScheme()
										.name("Auth JWT")
										.type(SecurityScheme.Type.HTTP)
										.scheme("Bearer")
										.bearerFormat("JWT")))
				.info(new Info()
						.title("API segura")
						.version("1.0.0")
						.contact(new Contact().email("rfdouro@gmail.com").name("Rômulo Douro"))
						.description("<h1>Exemplo de REST API</h1>"));
	}

}
