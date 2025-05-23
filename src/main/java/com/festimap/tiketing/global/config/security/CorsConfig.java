package com.festimap.tiketing.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "https://localhost:3000",
            "http://khucore.localhost:3000",
            "http://business.localhost:3000",
            "http://business.localhost:3001",
            "https://khucore.com",
            "https://wabaa.kr",
            "https://business.wabaa.kr",
            "https://adelante.wabauniv.com",
            "http://m.localhost:3000",
            "https://firefestivaljeju.com",
            "https://m.firefestivaljeju.com",
            "https://mokpowshow.co.kr",
            "https://m.mokpowshow.co.kr",
            "https://business.festimap.kr",
            "https://festival.business.festimap.kr"
    );

    @Bean
    public CorsConfigurationSource customCorsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With","withcredentials","X-Guest-Token"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
