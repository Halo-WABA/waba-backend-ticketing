package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.dto.EventCreateReqDto;
import com.festimap.tiketing.domain.event.repository.EventRepository;
import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class TickerServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private BlockingQueue<TicketRequest> ticketQueue;

    @InjectMocks
    private TicketService ticketService;

    private Event event;
    private Ticket ticket;

    @BeforeEach
    void setUp() {

        TicketRequest request = new TicketRequest();
        setField(request, "eventId", 1L);
        setField(request, "phoneNumber", "01012341234");
        setField(request, "ticketCount", 2);

        EventCreateReqDto eventCreateReqDto = new EventCreateReqDto(
                1L,
                "테스트 이벤트",
                LocalDateTime.of(2025, 4,13, 12, 0),
                1000
        );
        event = Event.from(eventCreateReqDto);
        setField(event, "id", 1L);
        setField(event, "remainingTickets", 3000);
        setField(event, "isFinished", false);
        setField(event, "prefix", "MW");
        setField(event, "createdAt", LocalDateTime.now());
        eventRepository.save(event);
        ticket = Ticket.of(request, event);
        setField(ticket, "issuedAt", LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    @Test
    void 큐에_요청이_성공적으로_추가되는지_검증(){
        //given
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(event));
        TicketRequest clientRequest = new TicketRequest();
        setField(clientRequest, "eventId", 1L);
        setField(clientRequest, "phoneNumber", "01012341234");
        setField(clientRequest, "ticketCount", 2);
        given(ticketQueue.offer(clientRequest)).willReturn(true);

        //when
        ticketService.offerQueue(clientRequest);

        //then
        verify(ticketQueue, times(1)).offer(any());
    }

    @Test
    void 요청이_큐에_정상적으로_들어갔는지_직접_확인(){
        //given
        given(eventRepository.findById(anyLong())).willReturn(Optional.of(event));
        TicketRequest clientRequest = new TicketRequest();
        setField(clientRequest, "eventId", 1L);
        setField(clientRequest, "phoneNumber", "01012341234");
        setField(clientRequest, "ticketCount", 2);
        ticketQueue = new ArrayBlockingQueue<>(100);
        setField(ticketService, "ticketQueue", ticketQueue);

        //when
        System.out.println("전 : 요청 큐 크기 : " + ticketQueue.size());
        ticketService.offerQueue(clientRequest);
        System.out.println("후 : 요청 큐 크기 : " + ticketQueue.size());

        // then
        assertThat(ticketQueue).contains(clientRequest);
    }

}
