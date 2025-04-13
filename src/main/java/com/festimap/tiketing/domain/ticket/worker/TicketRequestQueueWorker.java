package com.festimap.tiketing.domain.ticket.worker;


import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class TicketRequestQueueWorker {

    private final BlockingQueue<TicketRequest> ticketQueue;
    private final TicketService ticketService;

    @PostConstruct
    //  @Scheduled(cron = "0 0 0 * * *")
    public void start() {
        Executors.newSingleThreadExecutor().submit(this::processQueue);
    }

    private void processQueue() {
        while (true) {
            try {
                ticketService.reserve(ticketQueue.take());
            } catch (Exception e) {
                // TODO: log
            }
        }
    }
}
