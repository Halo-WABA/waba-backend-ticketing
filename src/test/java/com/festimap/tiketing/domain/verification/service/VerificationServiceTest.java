package com.festimap.tiketing.domain.verification.service;


import com.festimap.tiketing.domain.verification.Verification;
import com.festimap.tiketing.domain.verification.dto.VerificationCheckReqDto;
import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import com.festimap.tiketing.domain.verification.exception.VerificationNotFoundException;
import com.festimap.tiketing.domain.verification.repository.VerificationRepository;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import com.festimap.tiketing.infra.sms.SmsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class VerificationServiceTest {

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private SmsClient smsClient;

    @InjectMocks
    private VerificationService verificationService;

    private Verification verification;
    private VerificationReqDto verificationReqDto;

    @BeforeEach
    void setUp(){
        verificationReqDto = new VerificationReqDto("01012345678");
        verification = Verification.from(verificationReqDto);
        verification.updateVerificationCode();
    }

    @Test
    void 기존번호가_존재할때_정상_동작_테스트() {
        // given
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));
        doNothing().when(smsClient).send(any());

        // when
        verificationService.sendVerificationCode(verificationReqDto);

        // then
        verify(verificationRepository, times(1)).findByPhoneNumber("01012345678");
        verify(smsClient, times(1)).send(any());
    }

    @Test
    void 새로번호_생성할때_정상_동작_테스트(){
        //given
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.empty());
        given(verificationRepository.save(any())).willReturn(verification);
        doNothing().when(smsClient).send(any());

        // when
        verificationService.sendVerificationCode(verificationReqDto);

        // then
        verify(verificationRepository, times(1)).findByPhoneNumber("01012345678");
        verify(verificationRepository,times(1)).save(any());
        verify(smsClient, times(1)).send(any());
    }



    @Test
    void 인증코드_검증_성공_테스트(){
        //given
        VerificationCheckReqDto dto = new VerificationCheckReqDto(verification.getPhoneNumber(),verification.getCode());
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));

        //when
        verificationService.checkVerificationCode(dto);

        //then
        assertThat(verification.isVerified()).isTrue();
        assertThat(verification.getVerifiedExpiredAt()).isNotNull();
    }

    @Test
    void 인증코드_검증_실패_Verification이_존재하지_않을때_예외(){
        //given
        VerificationCheckReqDto dto = new VerificationCheckReqDto(verification.getPhoneNumber(),verification.getCode());
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> verificationService.checkVerificationCode(dto))
                .isInstanceOf(VerificationNotFoundException.class);
    }

    @Test
    void 인증코드_검증_실패_인증코드가_올바르지_않을_경우_예외(){
        //given
        VerificationCheckReqDto dto = new VerificationCheckReqDto(verification.getPhoneNumber(),"0");
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));

        //when & then
        assertThatThrownBy(() -> verificationService.checkVerificationCode(dto))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_VERIFICATION_CODE);
    }

    @Test
    void 인증기간이_지난_인증코드일_경우_예외(){
        //given
        VerificationCheckReqDto dto = new VerificationCheckReqDto(verification.getPhoneNumber(),verification.getCode());
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));
        setField(verification, "codeExpiredAt", LocalDateTime.now().minusMinutes(5));

        //when & then
        assertThatThrownBy(() -> verificationService.checkVerificationCode(dto))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.EXPIRED_VERIFICATION_CODE);;
    }
}
