package com.example.authservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components())
			.addServersItem(new Server().url("/"))
			.info(info());
	}

	private Info info() {
		return new Info()
			.title("Auth Service API")
			.description("Auth Service API Description")
			.version("1.0.0")
			.license(new License().name("Apache 2.0").url("http://springdoc.org"));
	}
}
