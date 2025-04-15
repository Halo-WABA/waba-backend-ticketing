package com.festimap.tiketing.domain.verification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class VerificationReqDto {

    @NotNull(message = "eventId는 필수입니다.")
    @Positive(message = "eventId는 0보다 커야 합니다.")
    private Long eventId;

    @Pattern(regexp = "^010\\d{8}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;
}
