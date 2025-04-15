package com.festimap.tiketing.domain.ticket.repository;


import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.dto.EventCreateReqDto;
import com.festimap.tiketing.domain.event.repository.EventRepository;
import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("NonAsciiCharacters")
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    private Ticket ticket;
    private Event event;

    @BeforeEach
    void setUp() {
        TicketRequest request = new TicketRequest();
        setField(request, "eventId", 1L);
        setField(request, "phoneNumber", "01012341234");
        setField(request, "ticketCount", 2);

        EventCreateReqDto eventCreateReqDto = new EventCreateReqDto(
                1L,
                "테스트 이벤트",
                LocalDateTime.now(),
                1000,
                "MW"
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
    void 이벤트ID_유저전화번호_조회_이벤트는_영속성컨텍스트에_있다(){
        Optional<Ticket> result = ticketRepository.findByEventIdAndPhoneNumberWithEvent(event.getId(), "01012341234");
        System.out.println("=======================");
        assertThat(result).isPresent();
        System.out.println("=======================");
        assertThat(result.get().getEvent()).isNotNull();
        System.out.println("=======================");
        assertThat(result.get().getEvent().getId()).isEqualTo(event.getId());
        System.out.println("=======================");
    }
}
