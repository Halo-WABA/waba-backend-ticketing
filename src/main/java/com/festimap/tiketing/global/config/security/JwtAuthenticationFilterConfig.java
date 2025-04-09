package com.festimap.tiketing.global.config.security;

import com.festimap.tiketing.global.filter.JwtAuthenticationFilter;
import com.festimap.tiketing.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilterConfig {
    private final JwtProvider jwtProvider;

    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
