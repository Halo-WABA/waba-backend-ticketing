package com.festimap.tiketing.domain.verification.controller;

import com.festimap.tiketing.domain.verification.dto.VerificationCheckReqDto;
import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import com.festimap.tiketing.domain.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/send/code")
    public void sendVerificationCode(@RequestBody VerificationReqDto verificationReqDto) {
        verificationService.sendVerificationCode(verificationReqDto);
    }

    @PostMapping("/check/code")
    public void checkVerificationCode(@RequestBody VerificationCheckReqDto verificationCheckReqDto) {
        verificationService.checkVerificationCode(verificationCheckReqDto);
    }
}
