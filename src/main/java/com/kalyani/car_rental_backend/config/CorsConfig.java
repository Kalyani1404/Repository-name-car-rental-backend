package com.kalyani.car_rental_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.*;

@Configuration
public class CorsConfig {
    @Value("${frontend.url:http://localhost:3000}") private String frontendUrl;
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration c=new CorsConfiguration();
        List<String> patterns=new ArrayList<>(List.of("http://localhost:*","http://127.0.0.1:*"));
        if(frontendUrl!=null&&!frontendUrl.isBlank()) patterns.add(frontendUrl.trim());
        c.setAllowedOriginPatterns(patterns);
        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setExposedHeaders(List.of("Authorization"));
        c.setAllowCredentials(true);c.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();source.registerCorsConfiguration("/**",c);return source;
    }
}
