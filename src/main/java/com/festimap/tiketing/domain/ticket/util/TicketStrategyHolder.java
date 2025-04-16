package com.festimap.tiketing.domain.ticket.util;

import com.festimap.tiketing.domain.ticket.TicketingStrategy;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import org.springframework.stereotype.Component;

@Component
public class TicketStrategyHolder {
    private TicketingStrategy currentStrategy = TicketingStrategy.LOCK;// 기본값 설정

    public void updateStrategy(TicketingStrategy strategy) {
        if(strategy.equals(TicketingStrategy.QUEUE)) {
            throw new BaseException(ErrorCode.NOT_SUPPORTED_STRATEGY);
        }
        this.currentStrategy = strategy;
    }

    public TicketingStrategy getStrategy() {
        return currentStrategy;
    }
}
