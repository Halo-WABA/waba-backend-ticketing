package com.festimap.tiketing.domain.verification;

import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class VerificationTest {

    private Verification verification;

    @BeforeEach
    void setUp(){
        VerificationReqDto verificationReqDto = new VerificationReqDto();
        setField(verificationReqDto, "eventId", 1L);
        setField(verificationReqDto, "phoneNumber", "01012345678");
        verification = Verification.from(verificationReqDto);
    }

    @Test
    void verification_생성_테스트(){
        //given
        VerificationReqDto verificationReqDto = new VerificationReqDto();
        setField(verificationReqDto, "eventId", 1L);
        setField(verificationReqDto, "phoneNumber", "01012345678");

        //when
        Verification target = Verification.from(verificationReqDto);

        //then
        assertThat(target).isNotNull();
        assertThat(target.getCode()).isNotNull();
        assertThat(target.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(target.isVerified()).isFalse();
    }

    @Test
    void verification_코드_생성(){
        //when
        verification.updateVerificationCode();

        //then
        assertThat(verification.isVerified()).isFalse();
        assertThat(verification.getCode()).isNotNull();
        assertThat(verification.getCodeExpiredAt()).isAfter(LocalDateTime.now());
    }

    @Test
    void verification_코드_검증_성공(){
        //when
        verification.verifyCode(verification.getCode());

        //then
        assertThat(verification.isVerified()).isTrue();
        assertThat(verification.getCodeExpiredAt()).isAfter(LocalDateTime.now());
    }

    @Test
    void verification_코드_틀릴때_예외(){
        //when & then
        assertThatThrownBy(() -> verification.verifyCode("0"))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_VERIFICATION_CODE);
    }

    @Test
    void verification_코드_기한_지났을_경우_예외(){
        //given
        setField(verification, "codeExpiredAt", LocalDateTime.now().minusMinutes(5));

        //when & then
        assertThatThrownBy(() -> verification.verifyCode(verification.getCode()))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.EXPIRED_VERIFICATION_CODE);
    }
}
