package com.festimap.tiketing.domain.ticket.service;


import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.dto.EventCreateReqDto;
import com.festimap.tiketing.domain.event.repository.EventRepository;
import com.festimap.tiketing.domain.ticket.Ticket;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.repository.TicketRepository;
import com.festimap.tiketing.domain.verification.Verification;
import com.festimap.tiketing.domain.verification.repository.VerificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class TicketConcurrencyTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    private Event event;

    @Autowired
    @Qualifier("lockBased")
    private TicketService ticketService;

    @BeforeEach
    void setUp(){
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        verificationRepository.deleteAll();

        EventCreateReqDto eventCreateReqDto =
                new EventCreateReqDto(1L,"1차 예매", LocalDateTime.now(),10,"MW");

        for (int i = 0; i < 20; i++) {
            Verification verification = Verification.builder().build();
            setField(verification, "phoneNumber", String.format("%d", i));
            setField(verification, "code", "123456"); // 임의의 고정 코드
            setField(verification, "codeExpiredAt", LocalDateTime.now().plusMinutes(3));
            setField(verification, "verifiedExpiredAt", LocalDateTime.now().plusMinutes(10));
            setField(verification, "isVerified", true); // 필요한 경우 true로 설정

            verificationRepository.save(verification);
        }


        event = eventRepository.save(Event.from(eventCreateReqDto));
    }

    @Test
    void 동시에_20명_티켓_1장씩_예매할때_최대_10장까지만_생성() throws Exception {
        //given
        int threadCount = 20;
        AtomicInteger errorCnt = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Long eventId = event.getId();

        //when
        for (int i = 0; i < threadCount; i++) {
            final int userIndex = i;
            executorService.submit(() -> {
                try {
                    TicketRequest request = new TicketRequest();
                    setField(request, "eventId", eventId);
                    setField(request, "phoneNumber", String.format("%d",userIndex));
                    setField(request, "ticketCount", 1);

                    ticketService.reserve(request);
                } catch (Exception e) {
                    errorCnt.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        List<Ticket> tickets = ticketRepository.findAll();
        Event event = eventRepository.findById(eventId).orElseThrow();

        assertThat(tickets).hasSize(10);
        assertThat(event.getRemainingTickets()).isZero();
        assertThat(errorCnt.get()).isEqualTo(10);
    }
}
