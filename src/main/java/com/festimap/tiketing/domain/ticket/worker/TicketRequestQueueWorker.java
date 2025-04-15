package com.festimap.tiketing.domain.ticket.worker;


import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.service.TicketService;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketRequestQueueWorker {

    private final BlockingQueue<TicketRequest> ticketQueue;
    private final TicketService ticketService;

    @PostConstruct
    public void start() {
        Executors.newSingleThreadExecutor().submit(this::processQueue);
    }

    private void processQueue() {
        while (true) {
            try {
                ticketService.reserve(ticketQueue.take());
            } catch (BaseException e) {
                log.warn("업무 예외 발생 - 사용자 요청 처리 실패: {}", e.getMessage());
            } catch (Exception e) {
                log.error("예상치 못한 시스템 예외 발생", e);
            }
        }
    }
}
