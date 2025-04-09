package com.festimap.tiketing.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festimap.tiketing.global.config.security.SecurityConfig;
import com.festimap.tiketing.global.testsupport.SecurityTestBeans;
import com.festimap.tiketing.global.testsupport.TestController;
import com.festimap.tiketing.global.util.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@AutoConfigureMockMvc
@Import({SecurityTestBeans.class, SecurityConfig.class})
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void JWT_토큰_내용_확인_테스트() throws Exception {
        //given
        String token = jwtProvider.createToken("test", List.of("ROLE_ADMIN"));

        //when & then
        mockMvc.perform(get("/api/test/security").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("테스트"));
    }

    @Test
    void 올바른_Bearer_접두사가_없는_토큰_테스트() throws Exception {
        //given
        String token = jwtProvider.createToken("test", List.of("ROLE_ADMIN"));

        //when & then
        mockMvc.perform(get("/api/test/security").header("Authorization", "nonBearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 잘못된_JWT_구조의_토큰_테스트() throws Exception {
        // given
        String invalidJwt = "Bearer just_something_invalid";

        //when & then
        mockMvc.perform(get("/api/test/security").header("Authorization", invalidJwt))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void NULL_토큰_테스트()throws Exception{
        // given
        String invalidJwt = "Bearer ";

        //when & then
        mockMvc.perform(get("/api/test/security").header("Authorization", invalidJwt))
                .andExpect(status().isUnauthorized());
    }
}
