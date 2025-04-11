package com.festimap.tiketing.infra.sms.common;

import com.festimap.tiketing.domain.verify_code.Verification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsSendRequest {
    private String to;
    private String message;

    @Builder
    private SmsSendRequest(String to, String message) {
        this.to = to;
        this.message = message;
    }

    public static SmsSendRequest of(String to, String message) {
        return SmsSendRequest.builder()
                .to(to)
                .message(message)
                .build();
    }

    public static SmsSendRequest from(Verification verification) {
        return SmsSendRequest.builder()
                .to(verification.getPhoneNumber())
                .message(verification.getCode())
                .build();
    }

    private static String makeMessage(String code) {
        return String.format(
                "[축제 디지털 가이드 페스티맵]\n" +
                "인증번호: %s\n" +
                "입장권 관련 문의: (카카오채널 링크)\n" +
                "예매 완료시 2-3일내로 발송됩니다.",
                code
        );
    }
}
