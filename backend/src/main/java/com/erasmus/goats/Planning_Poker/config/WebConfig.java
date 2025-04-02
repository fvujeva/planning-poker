package com.erasmus.goats.Planning_Poker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Disable CORS by allowing all origins, methods, and headers
        registry.addMapping("/**")
                .allowedOrigins("*") // Allow all origins
                .allowedMethods("*") // Allow all HTTP methods (GET, POST, etc.)
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (optional)
    }
}
