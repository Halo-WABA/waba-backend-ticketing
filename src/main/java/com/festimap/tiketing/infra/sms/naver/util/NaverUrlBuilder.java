package com.festimap.tiketing.infra.sms.naver.util;

public class NaverUrlBuilder {

    private static final String BASE_PATH = "/sms/v2/services/";
    private static final String MESSAGE_PATH = "/messages";

    public static String buildMessageEndpoint(String serviceId) {
        return BASE_PATH + serviceId + MESSAGE_PATH;
    }
}
