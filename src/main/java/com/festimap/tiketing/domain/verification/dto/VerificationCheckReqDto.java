package com.festimap.tiketing.domain.verification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationCheckReqDto {
    private String phoneNumber;
    private String code;

    public VerificationCheckReqDto(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }
}
