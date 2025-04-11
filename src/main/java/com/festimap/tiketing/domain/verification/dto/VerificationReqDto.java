package com.festimap.tiketing.domain.verification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationReqDto {
    private String phoneNumber;

    public VerificationReqDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
