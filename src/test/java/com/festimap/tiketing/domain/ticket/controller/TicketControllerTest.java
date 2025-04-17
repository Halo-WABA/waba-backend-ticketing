package com.festimap.tiketing.domain.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festimap.tiketing.domain.ticket.TicketingStrategy;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.service.QueueBasedTicketService;
import com.festimap.tiketing.domain.ticket.service.TicketService;
import com.festimap.tiketing.domain.ticket.service.TicketServiceFactory;
import com.festimap.tiketing.domain.ticket.util.TicketStrategyHolder;
import com.festimap.tiketing.global.config.security.SecurityConfig;
import com.festimap.tiketing.global.testsupport.ControllerTestSecurityBeans;
import com.festimap.tiketing.global.util.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @MockBean
    private TicketService ticketService;

    private TicketRequest request;

    @BeforeEach
    void setUp(){
        request = new TicketRequest();
        setField(request, "eventId", 1L);
        setField(request, "phoneNumber", "01012341234");
        setField(request, "ticketCount", 2);
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void 티켓발급요청_성공()throws Exception{
        //given
        given(strategyHolder.getStrategy()).willReturn(TicketingStrategy.LOCK);
        given(ticketServiceFactory.getService(any())).willReturn(ticketService);

        //when & then
        mockMvc.perform(post("/api/ticket/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        verify(ticketService, times(1)).reserve(any(TicketRequest.class));
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void 티켓_개수_2개보다_클때_예외() throws Exception{
        //given
        setField(request, "ticketCount", 3);

        //when & then
        mockMvc.perform(post("/api/ticket/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void eventId_누락됬을때_예외()throws Exception{
        //given
        setField(request, "eventId", null);

        //when & then
        mockMvc.perform(post("/api/ticket/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin",roles = "ADMIN")
    void 올바르지_않은_전화번호()throws Exception{
        //given
        setField(request, "phoneNumber", "010123412345");

        //when & then
        mockMvc.perform(post("/api/ticket/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
