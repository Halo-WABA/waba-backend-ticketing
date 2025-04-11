package com.festimap.tiketing.domain.verification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festimap.tiketing.domain.verification.dto.VerificationCheckReqDto;
import com.festimap.tiketing.domain.verification.dto.VerificationReqDto;
import com.festimap.tiketing.domain.verification.service.VerificationService;
import com.festimap.tiketing.global.config.security.SecurityConfig;
import com.festimap.tiketing.global.testsupport.ControllerTestSecurityBeans;
import com.festimap.tiketing.global.util.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerificationController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VerificationService verificationService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void 인증코드_전송_요청_테스트()throws Exception{
        //given
        VerificationReqDto verificationReqDto = new VerificationReqDto("01012345678");
        doNothing().when(verificationService).sendVerificationCode(any());

        //when & then
        mockMvc.perform(post("/api/verification/send/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationReqDto)))
                .andExpect(status().isOk());
        verify(verificationService, times(1)).sendVerificationCode(any());
    }

    @Test
    void 인증코드_검증_요청_테스트() throws Exception{
        //given
        VerificationCheckReqDto verificationCheckReqDto = new VerificationCheckReqDto(
                "01012345678","123456");
        doNothing().when(verificationService).checkVerificationCode(any());

        //when & then
        mockMvc.perform(post("/api/verification/check/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationCheckReqDto)))
                .andExpect(status().isOk());
        verify(verificationService, times(1)).checkVerificationCode(any());
    }
}
