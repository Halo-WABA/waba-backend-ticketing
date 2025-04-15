package com.festimap.tiketing.global.constants;

public class SecurityConstants {

    public static final String[] SWAGGER_URLS = {
            "/",
            "/swagger-ui/**",
            "/v3/**",
            "/swagger-ui.html",
    };

    public static final String[] PUBLIC_POST_URLS = {
            "/api/verification/send/code",
            "/api/verification/check/code",
            "/api/tickets/apply"
    };

    public static final String[] PUBLIC_GET_URLS = {
            "/api/event"
    };
}
