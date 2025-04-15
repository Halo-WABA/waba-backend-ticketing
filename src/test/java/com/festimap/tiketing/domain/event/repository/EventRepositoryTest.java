package com.festimap.tiketing.domain.event.repository;

import com.festimap.tiketing.domain.event.Event;
import com.festimap.tiketing.domain.event.dto.EventCreateReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(SpringExtension.class)
@Disabled
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("NonAsciiCharacters")
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlatformTransactionManager txManager;

    private Event event;
    private EventCreateReqDto eventCreateReqDto;

    @BeforeEach
    void setUp() {
        eventCreateReqDto = new EventCreateReqDto();
        setField(eventCreateReqDto,"festivalId",1L);
        setField(eventCreateReqDto,"name","이벤트1");
        setField(eventCreateReqDto,"openAt", LocalDateTime.now().plusMinutes(5));
        setField(eventCreateReqDto,"totalTickets",10);
        setField(eventCreateReqDto,"prefix","MW");
        event = Event.from(eventCreateReqDto);
        setField(event,"createdAt",LocalDateTime.now());

        event = eventRepository.save(event);
    }

    @Test
    void festivalId로_이벤트_목록_조회시_저장된_엔티티_반환() {
        // when
        List<Event> result = eventRepository.findAllByFestivalId(1L);

        // then
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Event::getId)
                .isEqualTo(event.getId());
    }

    @Test
    void id로_비관적_락을_획득_조회시_엔티티_반환() {
        // when
        Optional<Event> found = eventRepository.findByIdWithLock(event.getId());

        // then
        assertThat(found).isPresent().hasValueSatisfying(e ->
                        assertThat(e.getId()).isEqualTo(event.getId()));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 동시_비관적락_획득_시_두번째_트랜잭션은_타임아웃_예외() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        // -------- ① 첫 번째 트랜잭션 : 락 점유 후 12초 대기 -----------------
        executor.submit(() -> {
            TransactionTemplate tx = new TransactionTemplate(txManager);
            tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tx.executeWithoutResult(status -> {
                eventRepository.findByIdWithLock(event.getId()).orElseThrow();
                latch.countDown();
                try {
                    Thread.sleep(12_000);
                } catch (InterruptedException ignored) {

                }
            });
        });

        // -------- ② 두 번째 트랜잭션 : 락 획득 시도 -------------------------
        Future<Throwable> future = executor.submit(() -> {
            latch.await();
            TransactionTemplate tx = new TransactionTemplate(txManager);
            tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            try {
                tx.executeWithoutResult(s ->
                        eventRepository.findByIdWithLock(event.getId()));
                return null;
            } catch (Throwable t) {
                return t;
            }
        });

        Throwable thrown = future.get(15, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertThat(thrown)
                .isNotNull()
                .satisfies(t -> assertThat(t)
                        .isInstanceOfAny(TransactionSystemException.class));
    }
}
