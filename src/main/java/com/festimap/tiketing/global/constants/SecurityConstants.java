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
            "/api/ticket/apply"
    };

    public static final String[] PUBLIC_GET_URLS = {
            "/api/event",
            "/api/event/isOpen",
            "/api/guest/tickets"
    };

    public static final String[] ACTUATOR_URL = {
            "/actuator/prometheus"
    };

    public static final String[] PUBLIC_DELETE_URLS = {
            "/api/guest/tickets/*"
    };
}
