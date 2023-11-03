package com.sagitta.authservice.authentication.config;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CORS {
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@Nonnull CorsRegistry registry) {
				registry.addMapping("/**")
				        .allowedOrigins("*")
				        .allowedHeaders("*")
				        .allowedMethods("*");
			}
		};
	}
}
