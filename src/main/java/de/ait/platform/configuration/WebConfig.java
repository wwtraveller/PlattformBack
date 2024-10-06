package de.ait.platform.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*", "http://localhost:8080", "https://platform-qxs32.ondigitalocean.app", "https://platform-qxs32.ondigitalocean.app/api/swagger-ui/index.html", "https://platform-qxs32.ondigitalocean.app/api/swagger-ui/**")
                .allowedHeaders("Content-Type", "Accept", "Origin", "Authorization")
                .exposedHeaders("Location");
    }
}