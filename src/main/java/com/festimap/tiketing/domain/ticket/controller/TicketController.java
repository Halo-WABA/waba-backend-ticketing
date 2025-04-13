package com.festimap.tiketing.domain.ticket.controller;

import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TicketController {

    private final BlockingQueue<TicketRequest> ticketQueue;
    private AtomicLong requestOrder = new AtomicLong(0);

    @PostMapping("/tickets/apply")
    public String apply(@RequestBody TicketRequest request) {
        if (requestOrder.incrementAndGet() > 2400 || !ticketQueue.offer(request)) {
            throw new BaseException(ErrorCode.TICKET_RESERVATION_CLOSED);
        }
        return "신청됐습니다.";
    }
}
