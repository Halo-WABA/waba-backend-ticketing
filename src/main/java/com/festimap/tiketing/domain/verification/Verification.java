package com.festimap.tiketing.domain.verification;


import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Table(name = "verification",
        indexes = {@Index(name = "idx_phone_number", columnList = "phone_number")},
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_phone_number", columnNames = {"phone_number"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "phone_number", nullable = false, length = 11)
    private String phoneNumber;

    @Column(name = "code", nullable = false, length = 6)
    private String code;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "code_expired_at", nullable = false)
    private LocalDateTime codeExpiredAt;

    @Column(name = "verified_expired_at")
    private LocalDateTime verifiedExpiredAt;

    @Builder
    private Verification(String phoneNumber, String code, LocalDateTime codeExpiredAt, LocalDateTime verifiedExpiredAt) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.codeExpiredAt = codeExpiredAt;
        this.verifiedExpiredAt = verifiedExpiredAt;
    }

    public static Verification from(VerificationReqDto verificationReqDto){
        return Verification.builder()
                .phoneNumber(verificationReqDto.getPhoneNumber())
                .code(generateCode())
                .codeExpiredAt(LocalDateTime.now().plusMinutes(3))
                .build();
    }

    public void updateVerificationCode(){
        this.code = generateCode();
        this.codeExpiredAt =LocalDateTime.now().plusMinutes(3);
    }

    public void verifyCode(String code){
        if (!this.code.equals(code)) {
            throw new BaseException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        this.isVerified = true;
        this.verifiedExpiredAt = LocalDateTime.now().plusHours(1);
    }

    private static String generateCode(){
        Random random = new Random();
        return IntStream.range(0,6)
                .map(i->random.nextInt(10))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
