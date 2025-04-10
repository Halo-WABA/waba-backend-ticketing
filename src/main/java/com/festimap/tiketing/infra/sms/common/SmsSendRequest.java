package com.festimap.tiketing.infra.sms.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsSendRequest {
    private String to;
    private String subject;
    private String message;

    private SmsSendRequest(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public static SmsSendRequest of(String to, String subject, String message) {
        return new SmsSendRequest(to, subject, message);
    }
}
