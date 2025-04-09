package com.festimap.tiketing.global.config.security;

import com.festimap.tiketing.global.constants.SecurityConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthorizationConfig {

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(SecurityConstants.SWAGGER_URLS).permitAll()
                .anyRequest().authenticated();
    }
}
