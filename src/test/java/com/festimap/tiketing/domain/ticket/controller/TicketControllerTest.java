package com.festimap.tiketing.domain.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.service.QueueBasedTicketService;
import com.festimap.tiketing.domain.ticket.service.TicketServiceFactory;
import com.festimap.tiketing.domain.ticket.util.TicketStrategyHolder;
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

import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private TicketServiceFactory ticketServiceFactory;

    @MockBean
    private TicketStrategyHolder strategyHolder;

    @Test
    void 티켓발급요청_검증() throws Exception {
        TicketRequest request = new TicketRequest();
        setField(request, "eventId", 1L);
        setField(request, "phoneNumber", "01012341234");
        setField(request, "ticketCount", 3);

        mockMvc.perform(post("/api/ticket/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

    }
}
