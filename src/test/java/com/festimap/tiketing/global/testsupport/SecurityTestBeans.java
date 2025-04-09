package com.festimap.tiketing.global.testsupport;

import com.festimap.tiketing.global.config.security.AuthorizationConfig;
import com.festimap.tiketing.global.config.security.CorsConfig;
import com.festimap.tiketing.global.config.security.JwtAuthenticationFilterConfig;
import com.festimap.tiketing.global.config.security.SecurityExceptionFilterConfig;
import com.festimap.tiketing.global.util.JwtProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@TestConfiguration
public class SecurityTestBeans {

    @Bean
    @Qualifier("customCorsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfig().customCorsConfigurationSource();
    }

    @Bean
    public AuthorizationConfig authorizationConfig() {
        return new AuthorizationConfig();
    }

    @Bean
    public JwtAuthenticationFilterConfig jwtAuthenticationFilterConfig(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilterConfig(jwtProvider);
    }

    @Bean
    public SecurityExceptionFilterConfig securityExceptionFilterConfig(HandlerExceptionResolver handlerExceptionResolver) {
        return new SecurityExceptionFilterConfig(handlerExceptionResolver);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }
}