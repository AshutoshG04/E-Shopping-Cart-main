// PATH: src/main/java/com/verto/shop/VertoShopApplication.java
package com.verto.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// === NEW: Import Caching ===
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;

// === NEW: Enable Caching ===
@EnableCaching
@SpringBootApplication
public class VertoShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(VertoShopApplication.class, args);
    }



    //   CORS Configuration Bean
    // This allows the React frontend (running on a different port, e.g., 3000/5173) 
    // to successfully make API requests to the Java backend (running on 8080).
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allows access to all /api/ endpoints from the standard frontend development port.
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173", "http://localhost:3000") // Allow common React/Vite ports
                        .allowedMethods("GET", "POST", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
