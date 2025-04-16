package com.festimap.tiketing.domain.ticket.service;

import com.festimap.tiketing.domain.ticket.TicketingStrategy;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketServiceFactory {
    private final LockBasedTicketService lockBased;
    private final QueueBasedTicketService queueBased;

    public TicketService getService(TicketingStrategy strategy) {
        switch (strategy) {
            case LOCK:
                return lockBased;
            case QUEUE:
                return queueBased;
            default:
                throw new BaseException(ErrorCode.NOT_SUPPORTED_STRATEGY);
        }
    }
}
