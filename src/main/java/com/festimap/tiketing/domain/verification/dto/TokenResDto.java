package com.festimap.tiketing.domain.verification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResDto {
    private String token;

    public TokenResDto(String token) {
        this.token = token;
    }
}
