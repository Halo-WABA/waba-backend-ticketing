package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.exception.EventNotFoundException;
import com.festimap.tiketing.domain.event.repository.EventRepository;
import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.repository.TicketRepository;
import com.festimap.tiketing.domain.verification.Verification;
import com.festimap.tiketing.domain.verification.exception.VerificationNotFoundException;
import com.festimap.tiketing.domain.verification.repository.VerificationRepository;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class LockBasedTicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @InjectMocks
    private LockBasedTicketService lockBasedTicketService;

    private Verification verification;
    private Event event;
    private TicketRequest ticketRequest;

    @BeforeEach
    void setUp(){
        verification = Verification.builder().build();
        setField(verification,"isVerified", true);
        setField(verification,"verifiedExpiredAt", LocalDateTime.now().plusHours(1));

        event = Event.builder().build();
        setField(event,"openAt",LocalDateTime.now().minusMinutes(30));
        setField(event,"remainingTickets",10);

        ticketRequest = new TicketRequest();
        setField(ticketRequest,"eventId",1L);
        setField(ticketRequest,"phoneNumber","01012345678");
        setField(ticketRequest,"ticketCount",1);
    }

    @Test
    void 티켓예매_성공_테스트(){
        //given
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));
        given(eventRepository.findByIdWithLock(1L)).willReturn(Optional.of(event));
        given(ticketRepository.findByEventIdAndPhoneNumberWithEvent(any(),any())).willReturn(Optional.empty());
        given(ticketRepository.save(any())).willReturn(any());

        //when
        lockBasedTicketService.reserve(ticketRequest);

        //then
        verify(verificationRepository, times(1)).findByPhoneNumber("01012345678");
        verify(eventRepository, times(1)).findByIdWithLock(1L);
        verify(ticketRepository, times(1))
                .findByEventIdAndPhoneNumberWithEvent(any(), any());
        verify(ticketRepository, times(1)).save(any());
    }

    @Test
    void verification_isVerified_False_검증_실패_예외(){
        //given
        setField(verification,"isVerified", false);
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));

        //when & then
        assertThatThrownBy(()->lockBasedTicketService.reserve(ticketRequest))
                .isInstanceOf(BaseException.class).extracting("errorCode")
                .isEqualTo(ErrorCode.VERIFICATION_REQUIRED);
    }

    @Test
    void verification_없을때_검증_실패_예외(){
        //given
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(()->lockBasedTicketService.reserve(ticketRequest))
                .isInstanceOf(VerificationNotFoundException.class);
    }

    @Test
    void event_없을때_검증_예외(){
        //given
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));
        given(eventRepository.findByIdWithLock(1L)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(()->lockBasedTicketService.reserve(ticketRequest))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void event_오픈시간이_아닐때_검증_예외(){
        //given
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));
        setField(event,"openAt",LocalDateTime.now().plusHours(1));
        given(eventRepository.findByIdWithLock(1L)).willReturn(Optional.of(event));

        //when & then
        assertThatThrownBy(()->lockBasedTicketService.reserve(ticketRequest))
                .isInstanceOf(BaseException.class).extracting("errorCode")
                .isEqualTo(ErrorCode.TICKET_SERVER_NOT_OPEN);
    }

    @Test
    void 티켓_중복_예매_검증_예외(){
        //given
        Ticket ticket = Ticket.builder()
                .count(1)
                .phoneNo("01012345678")
                .event(event)
                .build();
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));
        given(eventRepository.findByIdWithLock(1L)).willReturn(Optional.of(event));
        given(ticketRepository.findByEventIdAndPhoneNumberWithEvent(any(),any()))
                .willReturn(Optional.of(ticket));

        //when & then
        assertThatThrownBy(()->lockBasedTicketService.reserve(ticketRequest))
                .isInstanceOf(BaseException.class).extracting("errorCode")
                .isEqualTo(ErrorCode.TICKET_EXIST_BY_PHONENUM);
    }

    @Test
    void 남은티켓이_구매할_티켓보다_적을때_검증_예외(){
        //given
        given(verificationRepository.findByPhoneNumber("01012345678")).willReturn(Optional.of(verification));
        setField(event,"remainingTickets",0);
        given(eventRepository.findByIdWithLock(1L)).willReturn(Optional.of(event));
        given(ticketRepository.findByEventIdAndPhoneNumberWithEvent(any(),any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(()->lockBasedTicketService.reserve(ticketRequest))
                .isInstanceOf(BaseException.class).extracting("errorCode")
                .isEqualTo(ErrorCode.TICKET_SOLD_OUT);
    }
}
