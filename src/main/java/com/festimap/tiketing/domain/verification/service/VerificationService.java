package com.festimap.tiketing.domain.verification.service;


import com.festimap.tiketing.domain.verification.Verification;
import com.festimap.tiketing.domain.verification.dto.VerificationCheckReqDto;
import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import com.festimap.tiketing.domain.verification.exception.VerificationNotFoundException;
import com.festimap.tiketing.domain.verification.repository.VerificationRepository;
import com.festimap.tiketing.infra.sms.SmsClient;
import com.festimap.tiketing.infra.sms.common.SmsSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final SmsClient smsClient;

    @Transactional
    public void sendVerificationCode(VerificationReqDto verificationReqDto) {
        Verification verification = verificationRepository.findByPhoneNumber(verificationReqDto.getPhoneNumber())
                .map(existing ->{
                    existing.updateVerificationCode();
                    return existing;
                })
                .orElseGet(() -> {
                    return verificationRepository.save(Verification.from(verificationReqDto));
                });

        smsClient.send(SmsSendRequest.from(verification));
    }

    @Transactional
    public void checkVerificationCode(VerificationCheckReqDto verificationCheckReqDto){
        Verification verification = verificationRepository.findByPhoneNumber(verificationCheckReqDto.getPhoneNumber())
                .orElseThrow(()-> new VerificationNotFoundException(verificationCheckReqDto.getPhoneNumber()));

        verification.verifyCode(verificationCheckReqDto.getCode());
    }
}
