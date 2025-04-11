package com.festimap.tiketing.global.config.queue;

import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Configuration
public class RequestQueueConfig {

    private static final int TICKET_REQUEST_QUEUE_SIZE = 2400;

    @Bean
    public BlockingQueue<TicketRequest> ticketQueue() {
        return new ArrayBlockingQueue<>(TICKET_REQUEST_QUEUE_SIZE);
    }
}