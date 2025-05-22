package com.festimap.tiketing.domain.verification.controller;

import com.festimap.tiketing.domain.verification.dto.TokenResDto;
import com.festimap.tiketing.domain.verification.dto.VerificationCheckReqDto;
import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import com.festimap.tiketing.domain.verification.service.VerificationService;
import com.festimap.tiketing.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationService verificationService;
    private final JwtProvider jwtProvider;

    @PostMapping("/send/code")
    public void sendVerificationCode(@RequestBody @Validated VerificationReqDto verificationReqDto) {
        verificationService.sendVerificationCode(verificationReqDto);
    }

    @PostMapping("/check/code")
    public TokenResDto checkVerificationCode(@RequestBody @Validated VerificationCheckReqDto verificationCheckReqDto) {
        verificationService.checkVerificationCode(verificationCheckReqDto);
        return new TokenResDto(jwtProvider.createGuestToken(verificationCheckReqDto.getPhoneNumber()));
    }
}
