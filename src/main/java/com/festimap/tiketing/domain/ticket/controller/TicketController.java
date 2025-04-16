package com.festimap.tiketing.domain.ticket.controller;

import com.festimap.tiketing.domain.ticket.TicketingStrategy;
import com.festimap.tiketing.domain.ticket.dto.TicketRequest;
import com.festimap.tiketing.domain.ticket.service.QueueBasedTicketService;
import com.festimap.tiketing.domain.ticket.service.TicketService;
import com.festimap.tiketing.domain.ticket.service.TicketServiceFactory;
import com.festimap.tiketing.domain.ticket.util.TicketStrategyHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TicketController {

    private final TicketServiceFactory ticketServiceFactory;
    private final TicketStrategyHolder strategyHolder;

    @PostMapping("/ticket/strategy")
    public ResponseEntity<String> updateStrategy(@RequestParam TicketingStrategy strategy) {
        strategyHolder.updateStrategy(strategy);
        return ResponseEntity.ok("전략이 " + strategy + "로 변경되었습니다.");
    }

    @GetMapping("/ticket/strategy")
    public ResponseEntity<String> getStrategy() {
        TicketingStrategy strategy = strategyHolder.getStrategy();
        return ResponseEntity.ok("현재 전략은 " + strategy + "입니다.");
    }

    @PostMapping("/ticket/apply")
    public void apply(@Validated @RequestBody TicketRequest request) {
        TicketingStrategy strategy = strategyHolder.getStrategy();
        TicketService service = ticketServiceFactory.getService(strategy);
        service.reserve(request);
    }
}
