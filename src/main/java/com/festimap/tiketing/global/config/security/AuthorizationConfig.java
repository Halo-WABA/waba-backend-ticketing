package com.festimap.tiketing.global.config.security;

import com.festimap.tiketing.global.constants.SecurityConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthorizationConfig {

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(SecurityConstants.SWAGGER_URLS).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PUBLIC_POST_URLS).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.PUBLIC_GET_URLS).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.ACTUATOR_URL).permitAll()
                .anyRequest().authenticated();
    }
}
