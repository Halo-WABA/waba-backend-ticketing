package com.festimap.tiketing.global.config.security;

import com.festimap.tiketing.global.filter.JwtAuthenticationFilter;
import com.festimap.tiketing.global.filter.SecurityExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
public class SecurityExceptionFilterConfig {

    private final HandlerExceptionResolver handlerExceptionResolver;

    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new SecurityExceptionFilter(handlerExceptionResolver), JwtAuthenticationFilter.class);
    }
}
