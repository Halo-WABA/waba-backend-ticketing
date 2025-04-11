package com.festimap.tiketing.domain.verification.repository;

import com.festimap.tiketing.domain.verification.Verification;
import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Disabled
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("NonAsciiCharacters")
public class VerificationRepositoryTest {

    @Autowired
    private VerificationRepository verificationRepository;

    private Verification verification;


    @BeforeEach
    void setUp() {
        VerificationReqDto verificationReqDto = new VerificationReqDto("01012345678");
        verification = verificationRepository.save(Verification.from(verificationReqDto));
    }

    @Test
    void Verification_생성(){
        assertThat(verification).isNotNull();
        assertThat(verification.getPhoneNumber()).isEqualTo("01012345678");
    }

    @Test
    void phoneNumber_로조회하기(){
        //when
        Optional<Verification> target = verificationRepository.findByPhoneNumber("01012345678");

        //then
        assertThat(target.isPresent()).isTrue();
        assertThat(target.get().getPhoneNumber()).isEqualTo("01012345678");
        assertThat(target.get().isVerified()).isFalse();
    }

}
